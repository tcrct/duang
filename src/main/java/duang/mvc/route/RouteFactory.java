package duang.mvc.route;

import duang.mvc.common.annotation.Controller;
import duang.mvc.common.annotation.Mapping;
import duang.mvc.common.enums.HttpMethod;
import duang.utils.ScanFactory;
import duang.utils.ToolsKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * 路由工厂
 *
 * @author Laotang
 * @since 1.0
 */
public class RouteFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteFactory.class);

    /**
     * 添加路由到Map集合
     */
    public static void addRoute() {
        Set<Class<?>> controllerClassSet = ScanFactory.getClassListByAnnotation(Controller.class);
        if (ToolsKit.isEmpty(controllerClassSet)) {
            LOGGER.warn("没有发现Controller类");
            return;
        }
        Set<String> excludedMethodName = ToolsKit.buildExcludedMethodName();
        for (Class<?> controllerClass : controllerClassSet) {
            Method[] methods = controllerClass.getMethods();
            if (null == methods) {
                continue;
            }
            for (Method method : methods) {
                if (excludedMethodName.contains(method.getName())) {
                    continue;
                }
                builderRequestMapping(method);


            }

        }


    }

    private static RequestMapping builderRequestMapping(Method method) {
        Mapping mappingAnnotation = method.getAnnotation(Mapping.class);
        String value = method.getName();
        String desc = value;
        String httpMethod = HttpMethod.ALL.name();
        if (null != mappingAnnotation) {
            value = mappingAnnotation.value();
            desc = mappingAnnotation.desc();
            httpMethod = mappingAnnotation.method().name();
        }
        RequestMapping requestMapping = new RequestMapping();
        return requestMapping;
    }

}
