package client.websocket;

import client.Menu;
import com.google.gson.Gson;
import jakarta.websocket.*;
import websocket.commands.UserGameCommand;

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
        menu.printNotification("Received: " + json);
    }

    public void connect(String authToken, int gameID) throws IOException {
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
        String json = new Gson().toJson(command);
        session.getBasicRemote().sendText(json);
    }
}
