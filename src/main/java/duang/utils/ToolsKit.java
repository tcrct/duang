package duang.utils;

import duang.mvc.core.dto.HeadDto;

import java.util.Collection;
import java.util.Map;

public class ToolsKit {

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

}
