package duang.server.undertow;

import cn.hutool.http.Header;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import duang.mvc.common.enums.HttpMethod;
import duang.mvc.http.IRequest;
import duang.utils.ToolsKit;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.HeaderValues;
import io.undertow.util.HttpString;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 转换Undertow请求
 *
 * @author Laotang
 */
public class UndertowRequest implements IRequest {

    private static final Log LOGGER = LogFactory.get(UndertowRequest.class);

    private String requestId;
    private HttpServerExchange exchange;
    private Map<String,String> headerMap = new ConcurrentHashMap<>();
    private Map<String,String> paramMap = new ConcurrentHashMap<>();

    public UndertowRequest(HttpServerExchange exchange) {
        this.requestId = createRequestId();
        this.exchange = exchange;
        init();
    }

    private void init() {
        createRequestHeaderMap();
        createRequestParamMap();
    }

    @Override
    public String requestId() {
        return requestId;
    }

    @Override
    public String body() {
        return null;
    }

    @Override
    public Byte[] bodyAsBytes() {
        return new Byte[0];
    }

    @Override
    public Long contentLength() {
        return exchange.getRequestContentLength();
    }

    @Override
    public String contentType() {
        return headerMap.get(Header.CONTENT_TYPE.name());
    }

    @Override
    public String contextPath() {
        return null;
    }

    @Override
    public Map<String, String> headers() {
        return headerMap;
    }

    @Override
    public String headers(String key) {
        return headerMap.get(key);
    }

    @Override
    public String host() {
        return exchange.getHostName();
    }

    @Override
    public String ip() {
        return exchange.getDestinationAddress().getAddress().getHostAddress();
    }

    @Override
    public String params(String key) {
        return paramMap.get(key);
    }

    @Override
    public Map<String, String> params() {
        return null;
    }

    @Override
    public String pathInfo() {
        return null;
    }

    @Override
    public Integer port() {
        return exchange.getDestinationAddress().getPort();
    }

    @Override
    public String protocol() {
        return null;
    }

    @Override
    public HttpMethod requestMethod() {
        return null;
    }

    @Override
    public String scheme() {
        return null;
    }

    @Override
    public String servletPath() {
        return null;
    }

    @Override
    public String uri() {
        return exchange.getRequestURI();
    }

    @Override
    public String url() {
       return exchange.getRequestURL();
    }

    @Override
    public UserAgent userAgent() {
        String userAgent =  headerMap.get(Header.USER_AGENT.toString());
        return ToolsKit.isNotEmpty(userAgent) ? UserAgentUtil.parse(userAgent) : null;
    }

    private void createRequestHeaderMap() {
        HeaderMap map = exchange.getRequestHeaders();
        if (ToolsKit.isEmpty(map)) {
            return;
        }
        for (Iterator<HeaderValues> iterator = map.iterator(); iterator.hasNext();) {
            String key = iterator.next().getHeaderName().toString();
            String value = map.getFirst(key);
            if (ToolsKit.isNotEmpty(key) && ToolsKit.isNotEmpty(value)) {
                headerMap.put(key, value);
            }
        }
    }

    private void createRequestParamMap() {
        Map<String, Deque<String>> pathParamMap = exchange.getPathParameters();
        Map<String, Deque<String>> queryParamMap = exchange.getQueryParameters();
        createReqMap(pathParamMap);
        createReqMap(queryParamMap);
    }

    private void createReqMap(Map<String, Deque<String>> subMap) {
        if (ToolsKit.isEmpty(subMap)) {
            return;
        }
        for (Iterator<Map.Entry<String, Deque<String>>> iterator = subMap.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry<String, Deque<String>> entry = iterator.next();
            String key = entry.getKey();
            String value = entry.getValue().peek();
            if (ToolsKit.isNotEmpty(key) && ToolsKit.isNotEmpty(value)) {
                this.paramMap.put(key, value);
            }
        }
    }
}
