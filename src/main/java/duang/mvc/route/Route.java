package duang.mvc.route;
import com.alibaba.fastjson.annotation.JSONField;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 路由对象
 *
 * @author Laotang
 * @since 1.0
 */
public class Route implements java.io.Serializable {

    /**
     * mapping注解对象类
     */
    private RequestMapping requestMapping;
    /**
     *执行的控制器类
     */
    private Class<?> controllerClass;
    /**
     * 执行的方法
     */
    @JSONField(serialize=false, deserialize = false)
    private Method method;
    /**
     * 请求的参数对象
     */
    private List<RequestParam> requestParamList;
    /**
     * 返回对象
     */
    private Class<?> returnClass;

    public Route() {
    }

    public Route(RequestMapping requestMapping, Class<?> controllerClass, Method method, List<RequestParam> requestParamList, Class<?> returnClass) {
        this.requestMapping = requestMapping;
        this.controllerClass = controllerClass;
        this.method = method;
        this.requestParamList = requestParamList;
        this.returnClass = returnClass;
    }

    public RequestMapping getRequestMapping() {
        return requestMapping;
    }

    public void setRequestMapping(RequestMapping requestMapping) {
        this.requestMapping = requestMapping;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public void setControllerClass(Class<?> controllerClass) {
        this.controllerClass = controllerClass;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public List<RequestParam> getRequestParamList() {
        return requestParamList;
    }

    public void setRequestParamList(List<RequestParam> requestParamList) {
        this.requestParamList = requestParamList;
    }

    public Class<?> getReturnClass() {
        return returnClass;
    }

    public void setReturnClass(Class<?> returnClass) {
        this.returnClass = returnClass;
    }
}
