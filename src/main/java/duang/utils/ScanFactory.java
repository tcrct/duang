package duang.utils;

import cn.hutool.core.util.ClassUtil;
import duang.exception.DuangException;
import duang.mvc.core.enums.ScanAnnotationEnum;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 *  扫描工厂类
 *
 * @author Laotang
 * @since 1.0
 */
public class ScanFactory {

    private static final Map<Class<?>, Set<Class<?>>> CLASS_MAP = new HashMap<>();

    /**
     * 扫描指定的项目路径下的所有class
     *
     * @param packagePath 项目路径前缀
     */
    public static void scanPackage(String packagePath) {
        if (ToolsKit.isEmpty(packagePath)) {
            throw new DuangException("项目包路径前缀不能为空");
        }

        if (!CLASS_MAP.isEmpty()) {
            return;
        }

        Set<Class<?>> classSet = ClassUtil.scanPackage(packagePath);
        if (ToolsKit.isEmpty(classSet)) {
            throw new DuangException(String.format("根据项目包路径[%s]没有扫描到class类，请检查路径是否正确！", packagePath));
        }

        //将所有添加了需要扫描的注解类，添加到Map
        for (Class<?> clazz : classSet) {
            Annotation[] annotations = clazz.getAnnotations();
            if (null != annotations && annotations.length > 0) {
                for (Annotation annotation : annotations) {
                    String name = annotation.getClass().getName();
                    // 找出需要扫描的注解类
                    Class<?> annotactionClass = ScanAnnotationEnum.getAnnotactionClass(name);
                    if (null != annotactionClass) {
                        Set<Class<?>> clazzSet = CLASS_MAP.get(annotactionClass);
                        if (null == clazzSet) {
                            clazzSet = new HashSet<>();
                        }
                        clazzSet.add(clazz);
                        CLASS_MAP.put(annotactionClass, clazzSet);
                    }
                }
            }
        }
    }

    /**
     * 取需要扫描的类集合
     * 其中key为需要扫描的注解类， value为添加了该注解的类List集合
     * @return
     */
    public static Map<Class<?>, Set<Class<?>>> getScanClassMap() {
        return CLASS_MAP;
    }

    /**
     * 根据注解类，取出添加了该注解类的所有类List集合
     * @param annotationClass 注解类
     * @return
     */
    public static Set<Class<?>> getClassListByAnnotation(Class<?> annotationClass) {
        return CLASS_MAP.get(annotationClass);
    }
}
