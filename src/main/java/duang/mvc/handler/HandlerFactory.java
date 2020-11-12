package duang.mvc.handler;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.http.HttpStatus;
import duang.exception.DuangException;
import duang.mvc.common.annotation.Handler;
import duang.mvc.http.IRequest;
import duang.mvc.http.IResponse;
import duang.mvc.route.RouteFactory;
import duang.spi.ResponseBodyAdvice;
import duang.utils.ScanFactory;
import duang.utils.ToolsKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.*;

/**
 * 处理器工厂
 * 当处理器链返回false或抛出异常时，中止程序执行，将异常信息抛出到前端
 *
 * @author Laotang
 * @since 1.0
 */
public class HandlerFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(HandlerFactory.class);
    public static final String LOGBACK_REQ_KEY = "request.uri";

    /**
     * Controller执行前的处理器集合
     */
    private static final List<IHandler> HANDLERS_BEFORE = new ArrayList<>();

    /**
     * Controller执行后的处理器集合
     */
    private static final List<IHandler> HANDLERS_AFTER = new ArrayList<>();

    static {
        if (HANDLERS_BEFORE.isEmpty()) {
            //添加初始化处理器，放在第1位
            HANDLERS_BEFORE.add(new InitDuangHandler());
            initHandler();
        }
    }

    public static void handler(IRequest request, IResponse response) {
        try {
            checkAccessTarget(request.uri());
            doBeforeChain(request, response);
            //调用Controller->Service->Dao
            ActionHandler.doHandler(request, response);
        } catch (Exception e) {
            if (ToolsKit.isNotEmpty(response.status()) && HttpStatus.HTTP_OK == response.status()) {
                response.status(HttpStatus.HTTP_INTERNAL_ERROR);
                response.body(ResponseBodyAdvice.duang().write(e));
            }
            LOGGER.warn("[{}]-请求[{}][{}]时出错: {}", DateUtil.formatDateTime(new Date()), request.uri(), request.requestId(), e.getMessage(), e);
        } finally {
            doAfterChain(request, response);
        }
    }

    /**
     * 在Controller执行前，执行前置处理器
     *
     * @param request
     * @param response
     * @return 如果任意一个handle返回false，则退出该请求处理
     */
    public static void doBeforeChain(IRequest request, IResponse response) {
        if (HANDLERS_BEFORE.isEmpty()) {
            return;
        }
        IHandler handler = null;
        try {
            for (Iterator<IHandler> iterator = HANDLERS_BEFORE.iterator(); iterator.hasNext(); ) {
                handler = iterator.next();
                handler.handler(request, response);
            }
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
    public static void doAfterChain(IRequest request, IResponse response) {
        if (HANDLERS_AFTER.isEmpty()) {
            MDC.remove(LOGBACK_REQ_KEY);
            return;
        }
        IHandler handler = null;
        try {
            for (Iterator<IHandler> iterator = HANDLERS_AFTER.iterator(); iterator.hasNext(); ) {
                handler = iterator.next();
                handler.handler(request, response);
            }
        } catch (Exception e) {
            LOGGER.warn("执行自定义的请求拦截处理器[{}]时抛出异常: {}", handler.getClass().getName(), e.getMessage(), e);
        } finally {
            MDC.remove(LOGBACK_REQ_KEY);
        }
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
                HANDLERS_BEFORE.addAll(handlerBeforeTreeMap.values());
            }
            if(!handlerAfterTreeMap.isEmpty()) {
                HANDLERS_AFTER.addAll(handlerAfterTreeMap.values());
            }
        } catch (Exception e) {
            LOGGER.warn("初始化请求拦截处理器时出错: {}，清空HANDLERS集合后退出", e.getMessage(), e);
            HANDLERS_BEFORE.clear();
            HANDLERS_AFTER.clear();
        }
    }

    /**
     * 取得request所请求的资源路径。
     * <p>
     * 资源路径为<code>getRequestURI()</code>。
     * </p>
     * <p>
     * 注意，<code>URI</code>以<code>"/"</code>开始，如果无内容，则返回空字符串
     *             <code>URI</code>不能以<code>"/"</code>结束，如果存在， 则强制移除
     * </p>
     */
    private static void checkAccessTarget(String target) {

        //  请求的URI是根路径或包含有.  则全部当作是静态文件的请求处理，直接返回
        if("/".equals(target) || target.contains(".") || ToolsKit.isEmpty(target)) {
            throw new DuangException("不支持根路径或静态文件访问");
        }

        // 分号后的字符截断
        if(target.contains(";")){
            target = target.substring(0,target.indexOf(";"));
        }
        // 去掉最后的/字符
        if(target.endsWith("/")) {
            target = target.substring(0, target.length()-1);
        }

        if(target.startsWith("//")) {
            throw new DuangException("请检查uri是否正确,若使用了nginx,注意proxy_pass不要以/结尾");
        }

        if (!RouteFactory.containsKey(target)) {
            throw new DuangException(String.format("该请求[%s]不合法!", target));
        }
    }
}
