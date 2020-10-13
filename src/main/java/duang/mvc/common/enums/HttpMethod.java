package duang.mvc.common.enums;

import java.util.HashMap;

/**
 * @author Laotang
 */
public enum HttpMethod {

    ALL, GET, POST, PUT, PATCH, DELETE, HEAD, TRACE, CONNECT, OPTIONS, BEFORE, AFTER, AFTERAFTER, UNSUPPORTED;

    private static HashMap<String, HttpMethod> methods = new HashMap<>();

    static {
        for (HttpMethod method : values()) {
            methods.put(method.toString(), method);
        }
    }


    public static HttpMethod get(String methodStr) {
        HttpMethod method = methods.get(methodStr.toUpperCase());
        return method != null ? method : UNSUPPORTED;
    }
}
