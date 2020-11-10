package duang.mvc.handler;

import cn.hutool.core.util.ReflectUtil;
import duang.exception.DuangException;
import duang.mvc.common.beans.BeanFactory;
import duang.mvc.common.core.ActionInvocation;
import duang.mvc.common.core.BaseController;
import duang.mvc.http.IRequest;
import duang.mvc.http.IResponse;
import duang.mvc.route.Route;
import duang.mvc.route.RouteFactory;
import duang.spi.ResponseBodyAdvice;
import duang.utils.DataType;
import duang.utils.ToolsKit;

import java.lang.reflect.Method;

final public class ActionHandler {

    /**
     *  执行请求处理器
     * @param request      请求对象
     * @param response    返回对象
     * @throws Exception
     */
    static void doHandler(IRequest request, IResponse response) throws Exception {
        String target = request.uri();
        Route route = RouteFactory.getRoute(target);
        if (ToolsKit.isEmpty(route)) {
            throw new DuangException(String.format("根据请求URI[%s]找不到对应的Route对象", target));
        }
        Class<?> controllerClass = route.getControllerClass();
        BaseController controller = null;
        //是否单例, 为保证线程安全，默认是多例
        if(route.isSingleton()){
            controller = BeanFactory.getBean(controllerClass);
        } else {
            // 如果不是设置为单例模式的话就每次请求都创建一个新的Controller对象
            controller = (BaseController) ReflectUtil.newInstance(controllerClass);
            // 还要重新执行Ioc注入
//            IocHelper.ioc(controller);
        }
        // 传入request, response到请求的Controller
        controller.init(request, response);
        // 取出方法对象
        Method method = route.getMethod();
        // 取消类型安全检测（可提高反射性能）
        method.setAccessible(true);
        //将请求参数生成数组对象注入
        // 反射执行方法
        ActionInvocation invocation = new ActionInvocation(route, controller, method, target);
        Object resultObj = invocation.invoke();
        if (ToolsKit.isEmpty(resultObj)) {
            throw new DuangException(String.format("该请求[%s]的响应不能为null", target));
        }
        // 将所有返回的数据类型则封装成ReturnDto返回
        response.body(ResponseBodyAdvice.duang().write(resultObj));
    }




}
