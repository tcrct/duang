package duang.mvc.common.enums;

import duang.mvc.common.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 *  需要扫描的注解枚举
 *
 * @author Laotang
 * @since 1.0
 */
public enum ScanAnnotation {

    CONTROLLER(Controller.class),
    SERVICE(Service.class),
    HANDLER(Handler.class),
    PLUGIN(Plugin.class),
    WEBSOCKET(WebSocket.class),
    ;

    private Class<?> annotationClass;
    private ScanAnnotation(Class<?> annotationClass) {
        this.annotationClass = annotationClass;
    }
    private Class<?> getAnnotationClass() {
        return annotationClass;
    }

    private static final Map<String, Class<?>> enumMap = new HashMap<>();
    public static Class<?> getAnnotactionClass(String annotationName) {
        if (enumMap.isEmpty()) {
            for (ScanAnnotation annotationEnum :  ScanAnnotation.values()) {
                Class<?> clazz = annotationEnum.getAnnotationClass();
                enumMap.put(clazz.getName(), clazz);
            }
        }
        return enumMap.get(annotationName);
    }
}
