package server;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/ws")
public class WebSocketHandler {

    @OnOpen
    public void onOpen(Session session){}

    @OnMessage
    public void onMessage(Session session, String message){}

    @OnClose
    public void onClose(Session session){}

    @OnError
    public void onError(Session session, Throwable error){}
}
