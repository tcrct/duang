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
import duang.utils.ScanFactory;
import duang.utils.SettingKit;
import duang.utils.ToolsKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Web Socket 工厂
 *
 * @author Laotang
 * @since 1.0
 */
public class WebSocketKit {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketKit.class);
    private static final WebSocketKit WEB_SOCKET_KIT = new WebSocketKit();
    private String key;
    private String message;

    public static WebSocketKit duang() {
        return WEB_SOCKET_KIT;
    }

    public WebSocketKit key(String webSocketUri) {
        this.key = webSocketUri;
        return this;
    }

    public WebSocketKit message(String sendMsg) {
        this.message = sendMsg;
        return this;
    }

    public void send() {
        WebSocketFactory.sendMsg(key, message);
    }
}
