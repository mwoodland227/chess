package client.websocket;

import client.Menu;
import com.google.gson.Gson;
import jakarta.websocket.*;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;

public class WebSocketFacade extends Endpoint{
    private final Menu menu;
    private Session session;


    public WebSocketFacade(String serverUrl, Menu menu) throws DeploymentException, IOException {
        this.menu = menu;
        String wsUrl = serverUrl.replaceFirst("^http", "ws") + "/ws";
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, URI.create(wsUrl));
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        session.addMessageHandler(String.class, this::handleMessage);
    }

    private void handleMessage(String json) {
        ServerMessage message = new Gson().fromJson(json, ServerMessage.class);
        switch (message.getServerMessageType()){
            case NOTIFICATION -> {
                NotificationMessage note = new Gson().fromJson(json, NotificationMessage.class);
                menu.printNotification(note.getNotification());
            }
            case ERROR -> {
                ErrorMessage error = new Gson().fromJson(json, ErrorMessage.class);
                menu.printError(error.getErrorMessage());
            }
            case LOAD_GAME -> {
                menu.printNotification("LOAD_GAME received");
            }
        }
    }

    public void connect(String authToken, int gameID) throws IOException {
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
        String json = new Gson().toJson(command);
        session.getBasicRemote().sendText(json);
    }
}
