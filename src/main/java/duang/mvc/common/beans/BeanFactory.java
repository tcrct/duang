package duang.mvc.common.beans;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import duang.mvc.route.RouteFactory;
import duang.utils.ScanFactory;
import duang.utils.ToolsKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;
import java.util.jar.Manifest;

/**
 * Bean工厂类，将ScanFactory扫描到的类进行实例化
 *
 * @author Laotang
 * @since 1.0
 */
public class BeanFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanFactory.class);

    /**
     * Bean Map
     * key为需要扫描的注解类class，value为标记了该注解的类对象Map集合，该Map的key为类，value为类的实例化对象
     * 注意：存在该Map下的对象为单例对象
     * */
    private static final Map<Class, Object> BEAN_MAP = new HashMap<>();

    public static <T> T getBean(Class<?> clazz) {
        Object object = BEAN_MAP.get(clazz);
        if (ToolsKit.isEmpty(object)) {
            LOGGER.warn("根据[%d]取出bean对象时，bean不存在");
            return null;
        }
        return (T)object;
    }

    /**
     * 初始化实例
     */
    public static void newInstance() {
        Map<Class<?>, Set<Class<?>>> classSetMap =  ScanFactory.getScanClassMap();
        if (ToolsKit.isEmpty(classSetMap)) {
            LOGGER.warn("ScanFactory没有扫描到任何需要实例化的类");
            return;
        }
        for (Iterator<Map.Entry<Class<?>, Set<Class<?>>>> iterator = classSetMap.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry<Class<?>, Set<Class<?>>> entry = iterator.next();
            Set<Class<?>> classValueSet = entry.getValue();
            if (ToolsKit.isEmpty(classValueSet)) {
                continue;
            }
            for (Class<?> clazz : classValueSet) {
                if (!ClassUtil.isPublic(clazz)) {
                    continue;
                }
                Object object = ReflectUtil.newInstance(clazz);
                if (ToolsKit.isEmpty(object)) {
                    continue;
                }
                BEAN_MAP.put(clazz, object);
            }
        }
    }




}
