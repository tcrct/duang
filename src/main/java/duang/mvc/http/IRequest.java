package duang.mvc.http;

import cn.hutool.http.useragent.UserAgent;
import duang.mvc.common.enums.HttpMethod;
import duang.utils.DuangId;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

public interface IRequest {

    default String createRequestId() {
        return DuangId.get().toString();
    }
    /**请求ID，唯一*/
    String requestId();
    /**请求主体内容，String格式*/
    String body();                   // request body sent by the client
    /**请求主体内容，Byte数组格式*/
    Byte[] bodyAsBytes();            // request body as bytes
    /**请求主体内容长度*/
    Long contentLength();          // length of request body
    /**请求主体内容类型*/
    String contentType();
    /**项目的上下文路径，如有设置，则会有所有URI路径前添加该上下文路径映射*/
    String contextPath();
    /**请求头Map集合*/
    Map<String,String> headers();
    /**根据key取出请求头的参数*/
    String headers(String key);
    /**请求域名*/
    String host();
    /**客户端IP*/
    String ip();
    /**根据key取出请求参数，包括get,post,path里的参数*/
    String params(String key);
    /**请求参数Map集合**/
    Map<String,String> params();
    /***/
    String pathInfo();
    /**请求端口*/
    Integer port();
    /**请求协议 e.g. http or https*/
    String protocol();
    /**请求方法(GET, POST)*/
    HttpMethod requestMethod();
    /**请求协议 e.g. http or https*/
    String scheme();
    String servletPath();
    /**请求URI*/
    URI uri() throws URISyntaxException;
    /**请求URL*/
    URL url() throws MalformedURLException;
    /**请求User Agent*/
    UserAgent userAgent();
}
