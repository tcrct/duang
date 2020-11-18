package duang.mvc.route;

import cn.hutool.core.util.StrUtil;
import duang.mvc.common.annotation.Controller;
import duang.mvc.common.annotation.Mapping;
import duang.mvc.common.enums.HttpMethod;
import duang.mvc.common.enums.MappingType;
import duang.mvc.common.enums.SettingKey;
import duang.utils.ScanFactory;
import duang.utils.SettingKit;
import duang.utils.ToolsKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * 路由工厂
 *
 * @author Laotang
 * @since 1.0
 */
final public class RouteFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteFactory.class);
    private static final TreeMap<String, Route> ROUTE_MAP = new TreeMap();

    static {
        initRoute();
        LOGGER.info("initRoute");
    }
    /**
     * 根据映射路由key取出路由对象
     * @param routeKey 映射路由key
     * @return 路由对象
     */
    public static Route getRoute(String routeKey) {
        return ROUTE_MAP.get(routeKey);
    }

    /**
     * 根据是否存在映射
     * @param routeKey
     * @return
     */
    public static boolean containsKey(String routeKey) {
        return ROUTE_MAP.containsKey(routeKey);
    }
    /**
     * 添加路由到Map集合
     */
    private static void initRoute() {
        Set<Class<?>> controllerClassSet = ScanFactory.getClassListByAnnotation(Controller.class);
        if (ToolsKit.isEmpty(controllerClassSet)) {
            LOGGER.warn("没有发现Controller类");
            return;
        }

        // 是否有设置统一的映射前缀
        String mappingPrefixPath = ToolsKit.getMappingPrefixPath();
        Set<String> excludedMethodName = ToolsKit.buildExcludedMethodName();
        for (Class<?> controllerClass : controllerClassSet) {
            // 创建controller的映射对象
            RequestMapping controllerRequestMapping = builderRequestMapping(controllerClass);
            Method[] methods = controllerClass.getMethods();
            if (null == methods) {
                continue;
            }
            String controllerMappringStr = ToolsKit.isEmpty(mappingPrefixPath)
                    ? controllerRequestMapping.getValue()
                    : mappingPrefixPath + controllerRequestMapping.getValue();
            for (Method method : methods) {
                if (excludedMethodName.contains(method.getName())) {
                    continue;
                }
                RequestMapping methodRequestMapping = builderRequestMapping(controllerMappringStr, method);
                List<RequestParam> requestParamList= builderRequestParameter(controllerClass.getName(), method);
                Class<?> returnClass = method.getReturnType();
                Route route = new Route(methodRequestMapping, controllerClass, method, requestParamList, returnClass);
                ROUTE_MAP.put(methodRequestMapping.getValue(), route);
            }
        }
        printRoute();
    }

    private static RequestMapping builderRequestMapping(Class<?> controllerClass) {
        Mapping controllerMapping = controllerClass.getAnnotation(Mapping.class);
        String value = controllerClass.getSimpleName();
        String desc = value;
        MappingType mappingType = MappingType.DIR;
        if (ToolsKit.isNotEmpty(controllerMapping)) {
            value = ToolsKit.isNotEmpty(controllerMapping.value()) ? controllerMapping.value() : value;
            desc = ToolsKit.isNotEmpty(controllerMapping.desc()) ? controllerMapping.desc() : desc;
            mappingType =  ToolsKit.isNotEmpty(controllerMapping.type()) ? controllerMapping.type() : mappingType;
        }
        value = value.startsWith("/") ? value : "/"+value;
        return new RequestMapping(value.toLowerCase(), desc, HttpMethod.ALL, mappingType);
    }

    private static RequestMapping builderRequestMapping(String controllerRequestMappingStr, Method method) {
        Mapping methodMapping = method.getAnnotation(Mapping.class);
        String value = method.getName();
        String desc = value;
        HttpMethod httpMethod = HttpMethod.ALL;
        MappingType mappingType = MappingType.MENU;
        if (null != methodMapping) {
            value = ToolsKit.isNotEmpty(methodMapping.value()) ? methodMapping.value() : value;
            desc = ToolsKit.isNotEmpty(methodMapping.desc()) ? methodMapping.desc() : desc;
            httpMethod = ToolsKit.isNotEmpty(methodMapping.method()) ? methodMapping.method() : httpMethod;
            mappingType = ToolsKit.isNotEmpty(methodMapping.type()) ? methodMapping.type() : mappingType;
        }
        value = value.startsWith("/") ? value : "/"+value;
        return new RequestMapping(controllerRequestMappingStr + value, desc, httpMethod, mappingType);
    }

    private static List<RequestParam> builderRequestParameter(String controllerName, Method method) {
        // 方法里的参数
        Parameter[] methodParameters = method.getParameters();
        List<RequestParam> paramList = new ArrayList<>();
        if (ToolsKit.isEmpty(methodParameters)) {
            LOGGER.debug("[{}]方法没有设置参数", controllerName+"."+method.getName());
            return paramList;
        }
        Class<?>[] methodParametersClass = method.getParameterTypes();
        Class<?> returnType = method.getReturnType();
        Annotation[] paramAnnotations = method.getAnnotations();
        if (ToolsKit.isNotEmpty(methodParameters)) {
            for (Parameter parameter : methodParameters) {
                paramList.add(new RequestParam(parameter.getAnnotations(), parameter.getType(), parameter.getName()));
                /*
                Class<?> paramType = parameter.getType();
                String name = parameter.getName();
                System.out.println(paramType+"              "+name+"          "+parameter);

                if (paramType.isAnnotationPresent(Bean.class)) {
                    Object bean = ReflectUtil.newInstance(paramType);
                    try {
                        VtorFactory.duang().validate(bean);
                    } catch (DuangException de) {
                        LOGGER.warn(de.getExceptionMsg());
                    }
                }

                Annotation[] annotations = parameter.getAnnotations();
                if (ToolsKit.isNotEmpty(annotations)) {
//                    for (Annotation annotation : annotations) {
                        try {
                            Object bean = ReflectUtil.newInstance(MainController.class);
                            Object[] parameterValues = {"3", 3};
                            VtorFactory.duang().validateParameters(bean, method, parameterValues);
                        } catch (DuangException de) {
                            LOGGER.warn(de.getExceptionMsg());
                        }
//                        System.out.println(annotation.annotationType());
//                    }
                }
                 */
            }
        }
        return paramList;
    }

    private static void printRoute() {
        if (ToolsKit.isEmpty(ROUTE_MAP)) {
            return;
        }

        LOGGER.warn("####### Route Mapping #######");
        for (Iterator<Map.Entry<String, Route>> iterator = ROUTE_MAP.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry<String, Route> entry = iterator.next();
            LOGGER.warn(entry.getKey());
        }
    }
}
