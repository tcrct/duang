package duang.mvc.common.core;

import cn.hutool.core.util.StrUtil;
import duang.exception.DuangException;
import duang.mvc.route.RequestParam;
import duang.mvc.route.Route;
import duang.utils.DataType;
import duang.utils.ToolsKit;
import duang.utils.TypeConverter;
import duang.valid.VtorFactory;


import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.List;

public class ActionInvocation {

    private BaseController controller;		// 要执行的Controller类
    private Route route;					// Controller类里的方法封装类对象
    private Interceptor[] inters;			// 拦截器，支持多个执行
    private Method method;		//执行的方法
    private int index = 0;
    private static final Object[] NULL_ARGS = new Object[0];		// 默认参数
    private String target;				// URI

    /**
     * 构造函数
     * @param route			Route对象
     * @param controller		Controller对象
     * @param method		方法对象
     * @param target	请求URI
     */
    public ActionInvocation(Route route, BaseController controller, Method method, String target) {
        this.route = route;
        this.controller = controller;
        this.method = method;
//        this.inters = route.getInterceptors();
        this.target = target;
    }

    public BaseController getController() {
        return controller;
    }

    public Route getRoute() {
        return route;
    }

    public Interceptor[] getInterceptors() {
        return inters;
    }

    public Method getMethod() {
        return method;
    }

    public String getTarget() {
        return target;
    }


    /**
     * Invoke the action. 反射方式执行该方法
     *
     * @throws Throwable
     */
    public Object invoke() throws Exception {
        Object returnObj = null;
        // 如果方法设置了拦截器，则先按书写顺序从上至下执行拦截器
        if (inters != null && index < inters.length) {
            inters[index++].intercept(this);
        } else {
            //如果方法体里有参数设置并且返回值不是void
            if (ToolsKit.isNotEmpty(method.getParameters())) {
                returnObj = method.invoke(controller, getParameterValues());
            } else {
                returnObj = method.invoke(controller, NULL_ARGS);
            }
        }
        return returnObj;
    }

    private Object[] getParameterValues() throws DuangException, ParseException {
        List<RequestParam> paramList = route.getRequestParamList();
        if (ToolsKit.isEmpty(paramList)) {
            return NULL_ARGS;
        }
        int size = paramList.size();
        Object[] requestParamValueObj = new Object[size];
        boolean isBeanParam = false;

        for (int i=0; i<size; i++) {
            RequestParam param  = paramList.get(i);
            String name = param.getName();
            if (ToolsKit.isEmpty(name)) {
                continue;
            }
            Class<?> paramClass = param.getParamClass();
            if (DataType.isBaseType(paramClass)) {
                String paramValue = controller.getRequest().params(name);
                if (ToolsKit.isEmpty(paramValue)) {
                    continue;
                }
                requestParamValueObj[i] = TypeConverter.convert(param.getParamClass(), paramValue);
            } else if (DataType.isBeanType(paramClass)){
                 Object bean = convertBean(paramClass);
                 if (ToolsKit.isEmpty(bean)) {
                     throw new DuangException("提交参数为Json数据时，转换为Dto时出错");
                 }
                VtorFactory.duang().validate(bean);
                requestParamValueObj[i] = bean;
                isBeanParam = true;
            }
        }
        if (!isBeanParam) {
            VtorFactory.duang().validateParameters(controller, method, requestParamValueObj);
        }
        return requestParamValueObj;
    }

    private Object convertBean(Class<?> paramClass) {
        String body = controller.getRequest().body();
        Object beanObj = null;
        if (ToolsKit.isNotEmpty(body)) {
            if (body.startsWith("{") && body.endsWith("}")) {
                beanObj = ToolsKit.jsonParseObject(body, paramClass);
            }
            else if (body.startsWith("[{") && body.endsWith("}]")) {
                beanObj = ToolsKit.jsonParseArray(body, List.class);
            }
        }
        return beanObj;
    }

}
