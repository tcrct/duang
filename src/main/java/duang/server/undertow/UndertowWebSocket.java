package duang.server.undertow;

import cn.hutool.core.date.DateUtil;
import duang.exception.DuangException;
import duang.mvc.common.beans.BeanFactory;
import duang.mvc.common.core.IWebSocket;
import duang.mvc.common.dto.HeadDto;
import duang.mvc.handler.HandlerFactory;
import duang.mvc.websocket.WebSocketFactory;
import duang.mvc.websocket.WebSocketSession;
import duang.spi.ResponseBodyAdvice;
import duang.utils.ToolsKit;
import io.undertow.websockets.WebSocketConnectionCallback;
import io.undertow.websockets.core.*;
import io.undertow.websockets.spi.WebSocketHttpExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.IOException;

/**
 * UndertowWebSocket
 *
 * @author Laotang
 */
public class UndertowWebSocket implements  WebSocketConnectionCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(UndertowWebSocket.class);

    /**
     *  链接成功回调方法
     * @param exchange WebSocketHttpExchange对象
     * @param channel WebSocketChannel对象
     */
    @Override
    public void onConnect(WebSocketHttpExchange exchange, WebSocketChannel channel) {

        if (!"ws".startsWith(channel.getRequestScheme())) {
            throw new DuangException("请求必须是ws协议");
        }
        String uri = exchange.getRequestURI();
        IWebSocket webSocket = WebSocketFactory.getWebSocket(uri);
        if (ToolsKit.isEmpty(webSocket)) {
            throw new DuangException(String.format("根据[%s]没有找到对应的IWebSocket对象，请确保路径正确", uri));
        }

        // 链接成功
        webSocket.connected(uri, channel);

        channel.getReceiveSetter().set(new AbstractReceiveListener() {

            @Override
            protected void onFullPongMessage(final WebSocketChannel channel, BufferedBinaryMessage message) throws IOException {
                LOGGER.info("onFullPongMessage");
                super.onFullPongMessage(channel, message);
            }

            @Override
            protected void onFullPingMessage(final WebSocketChannel channel, BufferedBinaryMessage message) throws IOException {
                LOGGER.info("onFullPingMessage");
                super.onFullPingMessage(channel, message);
            }

            @Override
            protected void onFullTextMessage(WebSocketChannel channel, BufferedTextMessage message) {
                LOGGER.info("onFullTextMessage: {}", channel.hashCode());
                if (!channel.isOpen()) {
                    throw new DuangException("WebSocket链接已关闭");
                }
                WebSocketSession session = createWebSocketSession(uri, channel, message.getData());
                try {

                    Object resultObj = webSocket.message(session);
                    if (ToolsKit.isEmpty(resultObj)) {
                        throw new DuangException("WebSocket返回值类型不能为void");
                    }
                    send(resultObj, channel);
                } catch (Exception e) {
                    LOGGER.warn(e.getMessage() ,e);
                    send(e, channel);
                }
            }
            @Override
            protected void onFullCloseMessage(final WebSocketChannel channel, BufferedBinaryMessage message) throws IOException {
                LOGGER.info("onFullCloseMessage");
                WebSocketSession session = createWebSocketSession(uri, channel, message.getData().toString());
                webSocket.closed(session);
                super.onFullCloseMessage(channel, message);
            }
        });
        // 通过恢复接收新帧
        channel.resumeReceives();
    }

    /**
     * 发送消息到客户端
     * @param resultObj 发送消息
     * @param channel   渠道对象
     */
    public static void send(Object resultObj, Object channel) {
        if (resultObj instanceof String) {
            LOGGER.warn(String.valueOf(resultObj));
        }
        WebSockets.sendText(ResponseBodyAdvice.duang().write(resultObj), (WebSocketChannel) channel, null);
    }

    /**
     * 创建WebSocketSession
     * @param uri ws的uri路径
     * @param channel  WebSocketChannel对象
     * @param message 接收到的消息
     * @return
     */
    private static WebSocketSession createWebSocketSession(String uri, WebSocketChannel channel, String message) {
        WebSocketSession<WebSocketChannel> socketSession = new WebSocketSession(uri);
        socketSession.setMessage(message);
        socketSession.setChannel(channel);
        createHeadDto(socketSession);
        return socketSession;
    }

    /**
     * create HeadDto，返回内容格式与http保持一致
     * @param socketSession
     */
    private static void createHeadDto(WebSocketSession socketSession) {
        HeadDto headDto = new HeadDto();
        headDto.setRequestId(socketSession.getId());
        headDto.setRequestTime(DateUtil.now());
        headDto.setUri(socketSession.getUri());
        ToolsKit.setThreadLocalDto(headDto);
        MDC.put(HandlerFactory.LOGBACK_REQ_KEY, headDto.getUri());
    }
}

