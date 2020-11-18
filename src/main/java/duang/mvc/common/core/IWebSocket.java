package duang.mvc.common.core;

import duang.exception.DuangException;
import duang.mvc.websocket.WebSocketSession;

/**
 * WebSocket 接口
 * @param <T> 接收请求处理逻辑后，需要返回的内容对象
 *
 * @author Laotang
 */
public interface IWebSocket<T> {

    /**
     * 链接成功时触发
     * @param uri 访问的URI地址
     * @param channelObj websocket channel对象
     * @throws DuangException
     */
    void connected(String uri, Object channelObj) throws DuangException;

    /**
     * 关闭时触发
     * @param session WebSocket Session 对象
     * @throws DuangException
     */
    void closed(WebSocketSession session) throws DuangException;

    /**
     *  接收客户端websocket
     * @param session WebSocket Session 对象
     * @return
     * @throws DuangException
     */
    T message(WebSocketSession session) throws DuangException;
}
