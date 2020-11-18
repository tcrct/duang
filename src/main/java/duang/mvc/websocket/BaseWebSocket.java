package duang.mvc.websocket;

import duang.exception.DuangException;
import duang.mvc.common.core.IWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  WebSocket 抽象类
 *  每一个BaseWebSocket类都要继承此抽象类或实现 IWebSocket
 * @param <T>  接收请求处理逻辑后，需要返回的内容对象
 *
 * @author Laotang
 */
public abstract class BaseWebSocket<T> implements IWebSocket<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseWebSocket.class);

    @Override
    public void connected(String key, Object channelObj) throws DuangException {
        if (!WebSocketFactory.containsChannelKey(key)) {
            LOGGER.info("websocket connected - uri:{}", key);
            WebSocketFactory.setSocketChannel(key, channelObj);
        }
    }

    @Override
    public void closed(WebSocketSession session) throws DuangException {
        LOGGER.info("websocket closed - requestId:{}, uri:{}", session.getId(), session.getUri());
        WebSocketFactory.removeSocketChannel(session.getUri());
    }
}
