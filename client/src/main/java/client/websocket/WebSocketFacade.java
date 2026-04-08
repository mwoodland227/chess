package client.websocket;

import chess.ChessMove;
import client.Menu;
import com.google.gson.Gson;
import jakarta.websocket.*;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.net.URI;

public class WebSocketFacade extends Endpoint{
    private final Menu menu;
    private Session session;


    public WebSocketFacade(String serverUrl, Menu menu) throws Exception  {
        this.menu = menu;
        String wsUrl = serverUrl.replaceFirst("^http", "ws") + "/ws";
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();

        container.setDefaultMaxSessionIdleTimeout(0);

        this.session = container.connectToServer(this, URI.create(wsUrl));

        this.session.setMaxIdleTimeout(0);
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        session.addMessageHandler(String.class, this::handleMessage);
    }

    @Override
    public void onClose(Session session, CloseReason closeReason){
        System.out.println("WebSocket close: " + closeReason);
    }

    @Override
    public void onError(Session session, Throwable thr) {
        System.out.println("WebSocket error: " + thr.getMessage());
        thr.printStackTrace();
    }

    private void handleMessage(String json) {
        ServerMessage message = new Gson().fromJson(json, ServerMessage.class);
        switch (message.getServerMessageType()){
            case NOTIFICATION -> {
                NotificationMessage note = new Gson().fromJson(json, NotificationMessage.class);
                menu.printNotification(note.getMessage());
            }
            case ERROR -> {
                ErrorMessage error = new Gson().fromJson(json, ErrorMessage.class);
                menu.printError(error.getErrorMessage());
            }
            case LOAD_GAME -> {
                LoadGameMessage load = new Gson().fromJson(json, LoadGameMessage.class);
                menu.loadGame(load.getGame());
            }
        }

    }

    private void sendCommand(UserGameCommand command) throws Exception{
        if(session == null || !session.isOpen()){
            throw new Exception("WebSocket connection closed");
        }
        session.getBasicRemote().sendText(new Gson().toJson(command));
    }

    public void connect(String authToken, int gameID) throws Exception {
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT,
                authToken, gameID, null);
        sendCommand(command);
    }


    public void makeMove(String authToken, int gameID, ChessMove move) throws Exception {
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE,
                authToken, gameID, move);

        sendCommand(command);
    }

    public void leave(String authToken, int gameID) throws Exception {
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.LEAVE,
                authToken, gameID, null);
        sendCommand(command);
    }

    public void resign(String authToken, int gameID) throws Exception {
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.RESIGN,
                authToken, gameID, null);
        sendCommand(command);
    }
}
