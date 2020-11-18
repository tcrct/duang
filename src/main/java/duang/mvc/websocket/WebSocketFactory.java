package duang.mvc.websocket;

import duang.exception.DuangException;
import duang.mvc.common.annotation.Mapping;
import duang.mvc.common.annotation.WebSocket;
import duang.mvc.common.beans.BeanFactory;
import duang.mvc.common.core.IWebSocket;
import duang.mvc.common.enums.HttpMethod;
import duang.mvc.common.enums.MappingType;
import duang.mvc.common.enums.SettingKey;
import duang.mvc.route.RequestMapping;
import duang.server.undertow.UndertowWebSocket;
import duang.utils.ScanFactory;
import duang.utils.SettingKit;
import duang.utils.ToolsKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Web Socket 工厂
 *
 * @author Laotang
 * @since 1.0
 */
final public class WebSocketFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketFactory.class);
    private static final TreeMap<String, IWebSocket> WEBSOCKET_MAP = new TreeMap();
    private static final Map<String, Object> CHANNEL_MAP = new ConcurrentHashMap<>();

    static {
        initWebSocket();
        LOGGER.info("initWebSocket");
    }
    /**
     * 根据映射路由key取出路由对象
     * @param webSocketKey 映射路由key
     * @return 路由对象
     */
    public static IWebSocket getWebSocket(String webSocketKey) {
        return WEBSOCKET_MAP.get(webSocketKey);
    }

    /**
     * 根据是否存在映射
     * @param webSocketKey
     * @return
     */
    public static boolean containsKey(String webSocketKey) {
        return WEBSOCKET_MAP.containsKey(webSocketKey);
    }
    /**
     * 添加路由到Map集合
     */
    private static void initWebSocket() {
        Set<Class<?>> webSocketClassSet = ScanFactory.getClassListByAnnotation(WebSocket.class);
        if (ToolsKit.isEmpty(webSocketClassSet)) {
            LOGGER.info("没有发现WebSocket类");
            return;
        }

        // 是否有设置统一的映射前缀
        String mappingPrefixPath = ToolsKit.getWsMappingPrefixPath();
        for (Class<?> webSocketClass : webSocketClassSet) {
            Class[] interfaces = webSocketClass.getInterfaces();
            if ((null != interfaces && interfaces.length>0 && !interfaces[0].equals(IWebSocket.class)) ||
                    !webSocketClass.getSuperclass().equals(BaseWebSocket.class)) {
                throw new DuangException(String.format("[%s]必须要实现IWebSocket接口类", webSocketClass.getName()));
            }
            // 创建webSocket的映射对象
            RequestMapping webSocketMapping = builderRequestMapping(webSocketClass);
            String webSocketMappringStr = checkPrefixPath(mappingPrefixPath, webSocketMapping.getValue());
            WEBSOCKET_MAP.put(webSocketMappringStr, BeanFactory.getBean(webSocketClass));
        }
        printRoute();
    }

    private static RequestMapping builderRequestMapping(Class<?> webSocketClass) {
        Mapping webSocketMapping = webSocketClass.getAnnotation(Mapping.class);
        String value = webSocketClass.getSimpleName();
        String desc = value;
        MappingType mappingType = MappingType.DIR;
        if (ToolsKit.isNotEmpty(webSocketMapping)) {
            value = ToolsKit.isNotEmpty(webSocketMapping.value()) ? webSocketMapping.value() : value;
            desc = ToolsKit.isNotEmpty(webSocketMapping.desc()) ? webSocketMapping.desc() : desc;
            mappingType =  ToolsKit.isNotEmpty(webSocketMapping.type()) ? webSocketMapping.type() : mappingType;
        }
        value = value.startsWith("/") ? value : "/"+value;
        return new RequestMapping(value.toLowerCase(), desc, HttpMethod.ALL, mappingType);
    }

    private static String checkPrefixPath(String prefix, String value) {
        String mappingPath = "/" + value.split("/")[1];
        if (!mappingPath.equals(prefix)) {
            throw new DuangException(String.format("WebSocket映射路径必须是以[%s]为第一层路径目录，如需更改路径前缀，请在[%s]里设置[%s]",
                    prefix, SettingKit.SETTING_FILE_NANE, SettingKey.WS_MAPPING_PREFIX_PATH.getKey()));
        }
        return value;
    }

    private static void printRoute() {
        if (ToolsKit.isEmpty(WEBSOCKET_MAP)) {
            return;
        }

        LOGGER.warn("####### WebSocket Mapping #######");
        for (Iterator<Map.Entry<String, IWebSocket>> iterator = WEBSOCKET_MAP.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry<String, IWebSocket> entry = iterator.next();
            LOGGER.warn(entry.getKey());
        }
    }

    protected static boolean containsChannelKey(String key) {
        return CHANNEL_MAP.containsKey(key);
    }
    protected static void setSocketChannel(String key, Object channelObj) {
        CHANNEL_MAP.put(key, channelObj);
    }

    protected static void removeSocketChannel(String key) {
        CHANNEL_MAP.remove(key);
    }

    protected static void sendMsg(String key, String msg) {
        Object channelObj = CHANNEL_MAP.get(key);
        if (ToolsKit.isEmpty(channelObj)) {
            throw new DuangException(String.format("根据[%s]找不到对应WebSocket Channel，请检查！", key));
        }
        UndertowWebSocket.send(msg, channelObj);
    }
}
