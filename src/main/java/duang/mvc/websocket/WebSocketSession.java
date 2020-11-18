package duang.mvc.websocket;

import duang.utils.DuangId;

public class WebSocketSession<T> {
    /**
     * 客户端发送过来的字符串内容，可根据业务自行定制字符串内容格式
     */
    private String message;
    /**
     * 异常
     */
    private Throwable cause;
    /**
     * URI路径
     */
    private String uri;
    /**
     * 自定义的WebSocketSession id, 格式为DuangId，作用与requestId一样，每次请求都会发生改变
     */
    private String id;

    private T channel;

    public WebSocketSession(String uri) {
        this.uri = uri;
        this.id = new DuangId().toString();
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }
    public Throwable getCause() {
        return cause;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public T getChannel() {
        return channel;
    }

    public void setChannel(T channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "WebSocketSession{" +
                "message='" + message + '\'' +
                ", cause=" + cause +
                ", uri='" + uri + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
