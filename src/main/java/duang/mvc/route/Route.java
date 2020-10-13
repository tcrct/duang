package duang.mvc.route;
import com.alibaba.fastjson.annotation.JSONField;
import duang.mvc.common.enums.HttpMethod;

import java.lang.reflect.Method;

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
     * Http 请求类型
     */
    private HttpMethod httpMethod;
    /**
     * 请求的参数对象
     */
    private RequestParam requestParam;

}
