package duang.mvc.core.enums;

import duang.mvc.core.annotation.Controller;
import duang.mvc.core.annotation.Handler;
import duang.mvc.core.annotation.Service;

import java.util.HashMap;
import java.util.Map;

/**
 *  需要扫描的注解枚举
 *
 * @author Laotang
 * @since 1.0
 */
public enum ScanAnnotationEnum {

    CONTROLLER(Controller.class),
    SERVICE(Service.class),
    HANDLER(Handler.class),

    ;

    private Class<?> annotationClass;
    private ScanAnnotationEnum(Class<?> annotationClass) {
        this.annotationClass = annotationClass;
    }
    private Class<?> getAnnotationClass() {
        return annotationClass;
    }

    private static final Map<String, Class<?>> enumMap = new HashMap<>();
    public static Class<?> getAnnotactionClass(String annotationName) {
        if (enumMap.isEmpty()) {
            for (ScanAnnotationEnum annotationEnum :  ScanAnnotationEnum.values()) {
                Class<?> clazz = annotationEnum.getAnnotationClass();
                enumMap.put(clazz.getName(), clazz);
            }
        }
        return enumMap.get(annotationName);
    }
}
