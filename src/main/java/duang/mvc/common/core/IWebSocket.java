package duang.mvc.common.core;

import duang.exception.DuangException;
import duang.mvc.websocket.WebSocketSession;

public interface IWebSocket<T> {

    void connected(String uri, Object channelObj) throws DuangException;

    void closed(WebSocketSession session) throws DuangException;

    T message(WebSocketSession session) throws DuangException;
}
