package com.duang.project.controller;

import duang.exception.DuangException;
import duang.mvc.common.annotation.Mapping;
import duang.mvc.common.annotation.WebSocket;
import duang.mvc.websocket.BaseWebSocket;
import duang.mvc.websocket.WebSocketSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebSocket
@Mapping(value = "/ws/main", desc = "主控制器")
public class MainWebSocket extends BaseWebSocket<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainWebSocket.class);

    @Override
    public String message(WebSocketSession session) throws DuangException {
        LOGGER.warn("############: {}", session.getMessage());
        return session.toString();
    }
}
