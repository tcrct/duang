package duang.server.undertow;

import cn.hutool.http.ContentType;
import cn.hutool.http.HttpStatus;
import duang.mvc.http.IRequest;
import duang.mvc.http.IResponse;

public class UndertowResponse implements IResponse {

    private String requestId;

    public UndertowResponse(IRequest request) {
        this.requestId = request.requestId();
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
    public void body(String content) {

    }

    @Override
    public void header(String key, String value) {

    }

    @Override
    public void redirect(String redirectPath) {

    }

    @Override
    public Integer status() {
        return null;
    }

    @Override
    public void status(HttpStatus status) {

    }

    @Override
    public ContentType type() {
        return null;
    }

    @Override
    public void type(ContentType contentType) {

    }
}
