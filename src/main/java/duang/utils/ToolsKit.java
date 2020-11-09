package duang.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import duang.mvc.common.dto.HeadDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ToolsKit {

    private static Logger LOGGER = LoggerFactory.getLogger(ToolsKit.class);

    public static SerializerFeature[] serializerFeatureArray = {
            SerializerFeature.QuoteFieldNames,
            SerializerFeature.WriteNonStringKeyAsString,
            SerializerFeature.DisableCircularReferenceDetect,
            SerializerFeature.NotWriteRootClassName,
            SerializerFeature.WriteDateUseDateFormat
    };
    private static SerializeConfig jsonConfig = new SerializeConfig();

    // 定义一个请求对象安全线程类
    private static DuangThreadLocal<HeadDto> requestHeaderThreadLocal = new DuangThreadLocal<HeadDto>() {
        @Override
        public HeadDto initialValue() {
            return new HeadDto();
        }
    };

    /**
     * 设置请求头DTO到ThreadLocal变量
     * @param headDto       请求头DTO
     */
    public static void setThreadLocalDto(HeadDto headDto) {
        requestHeaderThreadLocal.set(headDto);
    }

    /***
     * 判断传入的对象是否为空
     *
     * @param obj
     *            待检查的对象
     * @return 返回的布尔值, 为空或等于0时返回true
     */
    public static boolean isEmpty(Object obj) {
        return checkObjectIsEmpty(obj, true);
    }

    /***
     * 判断传入的对象是否不为空
     *
     * @param obj
     *            待检查的对象
     * @return 返回的布尔值, 不为空或不等于0时返回true
     */
    public static boolean isNotEmpty(Object obj) {
        return checkObjectIsEmpty(obj, false);
    }

    @SuppressWarnings("rawtypes")
    private static boolean checkObjectIsEmpty(Object obj, boolean bool) {
        if (null == obj) {
            return bool;
        } else if (obj == "") {
            return bool;
        } else if (obj instanceof Integer || obj instanceof Long || obj instanceof Double) {
            try {
                Double.parseDouble(obj + "");
            } catch (Exception e) {
                return bool;
            }
        } else if (obj instanceof String) {
            if (((String) obj).length() <= 0) {
                return bool;
            }
            if ("null".equalsIgnoreCase(obj + "")) {
                return bool;
            }
        } else if (obj instanceof Map) {
            if (((Map) obj).size() == 0) {
                return bool;
            }
        } else if (obj instanceof Collection) {
            if (((Collection) obj).size() == 0) {
                return bool;
            }
        } else if (obj instanceof Object[]) {
            if (((Object[]) obj).length == 0) {
                return bool;
            }
        }
        return !bool;
    }

    /**
     * 构建过滤方法名集合，默认包含Object类里公共方法
     * @param excludeMethodClass  如果有指定，则添加指定类下所有方法名
     *
     * @return
     */
    private static final Set<String> excludedMethodName = new HashSet<>();
    public static Set<String> buildExcludedMethodName(Class<?>... excludeMethodClass) {
        if(excludedMethodName.isEmpty()) {
            Method[] objectMethods = Object.class.getDeclaredMethods();
            for (Method m : objectMethods) {
                excludedMethodName.add(m.getName());
            }
        }
        Set<String> tmpExcludeMethodName = null;
        if(null != excludeMethodClass) {
            tmpExcludeMethodName = new HashSet<>();
            for (Class excludeClass : excludeMethodClass) {
                Method[] excludeMethods = excludeClass.getDeclaredMethods();
                if (null != excludeMethods) {
                    for (Method method : excludeMethods) {
                        tmpExcludeMethodName.add(method.getName());
                    }
                }
            }
            tmpExcludeMethodName.addAll(excludedMethodName);
        }
        return (null == tmpExcludeMethodName) ? excludedMethodName : tmpExcludeMethodName;
    }

    public static String toJsonString(Object obj) {
        return JSON.toJSONString(obj, jsonConfig, serializerFeatureArray);
    }

}
