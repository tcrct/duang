package duang.mvc.http;

import cn.hutool.http.ContentType;
import cn.hutool.http.HttpStatus;

public interface IResponse {
    /**请求ID*/
    String requestId();
    /**取响应内容*/
    String body();
    /**设置响应内容*/
    void body(String content);
    /**设置响应头*/
    void header(String key, String value);
    /**重定向*/
    void redirect(String redirectPath);
    /**响应状态*/
    Integer status();
    /**设置响应状态*/
    void status(HttpStatus status);
    /**内容主体的类型*/
    ContentType type();
    /**设置内容主体的类型*/
    void type(ContentType contentType);
}
