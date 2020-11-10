package duang.server.undertow;

import cn.hutool.http.ContentType;
import cn.hutool.http.HttpStatus;
import duang.mvc.http.IRequest;
import duang.mvc.http.IResponse;
import duang.utils.ToolsKit;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.HeaderValues;
import io.undertow.util.Headers;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UndertowResponse implements IResponse {

    private String requestId;
    private String content;
    private HttpServerExchange exchange;
    private Integer httpStatus;
    private Map<String,String> headerMap = new ConcurrentHashMap<>();

    public UndertowResponse(IRequest request, HttpServerExchange exchange) {
        this.requestId = request.requestId();
        this.exchange = exchange;
        init();
    }

    private void init() {
        createResponseHeaderMap();
    }

    private void createResponseHeaderMap() {
        HeaderMap map = exchange.getResponseHeaders();
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

    @Override
    public String requestId() {
        return requestId;
    }

    @Override
    public String body() {
        return content;
    }

    @Override
    public void body(String content) {
        this.content = content;
    }

    @Override
    public Map<String,String> header() {
        return headerMap;
    }

    @Override
    public void header(String key, String value) {
        headerMap.put(key, value);
    }

    @Override
    public void redirect(String redirectPath) {

    }

    @Override
    public Integer status() {
        return ToolsKit.isEmpty(httpStatus) ? HttpStatus.HTTP_OK : Integer.parseInt(httpStatus.toString());
    }

    @Override
    public void status(Integer status) {
        this.httpStatus = status;
    }

    @Override
    public ContentType type() {
        return ContentType.valueOf(exchange.getResponseHeaders().getFirst(Headers.CONTENT_TYPE).toUpperCase());
    }

    @Override
    public void type(ContentType contentType) {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, contentType.getValue());
    }
}
