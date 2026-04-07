package server;

import com.google.gson.Gson;
import dataaccess.*;
import handler.Handler;
import io.javalin.*;
import websocket.commands.UserGameCommand;
import server.WebSocketHandler.*;

public class Server {
    public final Handler handle;
    public final WebSocketHandler webSocketHandler;
    public final UserDAO userDAO;
    public final GameDAO gameDAO;

    private final Javalin javalin;

    public Server() {
        try{
            this.gameDAO = new MySqlGame();
            this.userDAO = new MySqlUser();
            DatabaseManager.configureDatabase();
        } catch (DataAccessException e){
            throw new RuntimeException(e);
        }
        this.handle = new Handler(userDAO, gameDAO);

        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        javalin.post("/user", handle::handleRegister);

        javalin.post("/session", handle::handleLogin);

        javalin.delete("/session", handle::handleLogout);

        javalin.get("/game", handle::handleListGames);

        javalin.post("/game", handle::handleCreateGame);

        javalin.delete("/db", handle::handleClear);

        javalin.put("/game", handle::handleJoinGame);

        javalin.ws("/ws", ws -> {
            ws.onConnect(ctx -> { });
            ws.onMessage(ctx -> {
                UserGameCommand command = new Gson().fromJson(ctx.message(), UserGameCommand.class);
                switch (command.getCommandType()) {
                    case CONNECT -> new WebSocketHandler(userDAO, gameDAO).connect(ctx, command);
                    case MAKE_MOVE -> new WebSocketHandler(userDAO, gameDAO).makeMove(ctx, command);
                    case LEAVE -> new WebSocketHandler(userDAO, gameDAO).leave(ctx, command);
                    case RESIGN -> new WebSocketHandler(userDAO, gameDAO).resign(ctx, command);

            });
            ws.onClose(ctx -> { });
            ws.onError(ctx -> { });
        });

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
