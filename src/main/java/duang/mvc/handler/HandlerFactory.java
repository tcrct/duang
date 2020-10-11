package duang.mvc.handler;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.setting.Setting;
import cn.hutool.setting.SettingUtil;
import duang.exception.DuangException;
import duang.mvc.core.annotation.Handler;
import duang.mvc.http.IRequest;
import duang.mvc.http.IResponse;
import duang.utils.ScanFactory;
import duang.utils.ToolsKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

/**
 * 处理器工厂
 *
 * @author Laotang
 * @since 1.0
 */
public class HandlerFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(HandlerFactory.class);

    /**
     * Controller执行前的处理器集合
     */
    private static final List<IHandler> HANDLERS = new ArrayList<>();

    /**
     * Controller执行后的处理器集合
     */
    private static final List<IHandler> HANDLERS_AFTER = new ArrayList<>();

    /**
     * 在Controller执行前，执行前置处理器
     *
     * @param request
     * @param response
     * @return 如果任意一个handle返回false，则退出该请求处理
     */
    public static boolean handler(IRequest request, IResponse response) {

        if (HANDLERS.isEmpty()) {
            //添加初始化处理器，放在第1位
            HANDLERS.add(new InitDuangHandler());
            initHandler();
        }
        IHandler handler = null;
        try {
            for (Iterator<IHandler> iterator = HANDLERS.iterator(); iterator.hasNext(); ) {
                handler = iterator.next();
                boolean isNext = handler.handler(request, response);
                if (!isNext) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            LOGGER.warn("执行自定义的请求拦截处理器[{}]时抛出异常: {}", handler.getClass().getName(), e.getMessage(), e);
            throw new DuangException(e.getMessage());
        }
    }

    /**
     * 在Controller执行后，执行后置处理器
     *
     * @param request
     * @param response
     * @return
     */
    public static boolean handlerAfter(IRequest request, IResponse response) {
        IHandler handler = null;
        try {
            for (Iterator<IHandler> iterator = HANDLERS_AFTER.iterator(); iterator.hasNext(); ) {
                iterator.next().handler(request, response);
            }
        } catch (Exception e) {
            LOGGER.warn("执行自定义的请求拦截处理器[{}]时抛出异常: {}", handler.getClass().getName(), e.getMessage(), e);
        }
        return true;
    }

    /**
     * 初始化请求拦截处理器
     * 扫描添加所有带有@Handler的类
     */
    private static void initHandler() {
        Set<Class<?>> handlerSet = ScanFactory.getClassListByAnnotation(Handler.class);
        if (ToolsKit.isEmpty(handlerSet)) {
            LOGGER.warn("没有自定义的请求拦截处理器");
            return;
        }
        try {
            TreeMap<Integer, IHandler> handlerBeforeTreeMap = new TreeMap<>();
            TreeMap<Integer, IHandler> handlerAfterTreeMap = new TreeMap<>();
            for (Class<?> clazz : handlerSet) {
                if (!IHandler.class.equals(clazz.getInterfaces()[0])) {
                    throw new RuntimeException(String.format("[%s]没有实现[%s]接口，请检查！", clazz.getName(), Handler.class.getSimpleName()));
                }
                Handler handlerAnn = clazz.getAnnotation(Handler.class);
                if (null == handlerAnn) {
                    continue;
                }
                IHandler handler = (IHandler) ReflectUtil.newInstance(clazz);
                if (ToolsKit.isNotEmpty(handler)) {
                    if (handlerAnn.after()) {
                        handlerAfterTreeMap.put(handlerAnn.sort(), handler);
                    } else {
                        handlerBeforeTreeMap.put(handlerAnn.sort(), handler);
                    }
                }
            }

            if(!handlerBeforeTreeMap.isEmpty()) {
                HANDLERS.addAll(handlerBeforeTreeMap.values());
            }
            if(!handlerAfterTreeMap.isEmpty()) {
                HANDLERS_AFTER.addAll(handlerAfterTreeMap.values());
            }
        } catch (Exception e) {
            LOGGER.warn("初始化请求拦截处理器时出错: {}，清空HANDLERS集合后退出", e.getMessage(), e);
            HANDLERS.clear();
        }
    }
}
