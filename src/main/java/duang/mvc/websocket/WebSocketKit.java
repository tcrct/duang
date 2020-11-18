package duang.mvc.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Web Socket 工厂
 *
 * @author Laotang
 * @since 1.0
 */
public class WebSocketKit {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketKit.class);
    private static final WebSocketKit WEB_SOCKET_KIT = new WebSocketKit();
    private String key;
    private String message;

    public static WebSocketKit duang() {
        return WEB_SOCKET_KIT;
    }

    public WebSocketKit key(String webSocketUri) {
        this.key = webSocketUri;
        return this;
    }

    public WebSocketKit message(String sendMsg) {
        this.message = sendMsg;
        return this;
    }

    public void send() {
        WebSocketFactory.sendMsg(key, message);
    }
}
