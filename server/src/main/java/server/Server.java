package server;

import com.google.gson.Gson;
import dataaccess.*;
import handler.Handler;
import io.javalin.*;
import io.javalin.websocket.WsContext;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;

public class Server {
    public final Handler handle;
    public final WebSocketHandler webSocketHandler;
    public final UserDAO userDAO;
    public final GameDAO gameDAO;

    private final Javalin javalin;

    public Server() {
        try{
            DatabaseManager.configureDatabase(); // switched this to initialize this before game and user DAO
            this.gameDAO = new MySqlGame();
            this.userDAO = new MySqlUser();
        } catch (DataAccessException e){
            throw new RuntimeException(e);
        }
        this.handle = new Handler(userDAO, gameDAO);
        this.webSocketHandler = new WebSocketHandler(userDAO, gameDAO);

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
            ws.onConnect(ctx -> {
                System.out.println("WS connected");
                ctx.enableAutomaticPings();
            });

            ws.onMessage(ctx -> handleWsMessage(ctx, ctx.message()));
            // replace below with this ^

//            ws.onMessage(ctx -> {
//                try {
//                    UserGameCommand command = new Gson().fromJson(ctx.message(), UserGameCommand.class);
//
//                    if (command == null || command.getCommandType() == null) {
//                        ctx.send(new Gson().toJson(new ErrorMessage("invalid command")));
//                        return;
//                    }
//
//                    switch (command.getCommandType()) {
//                        case CONNECT -> webSocketHandler.connect(ctx, command);
//                        case MAKE_MOVE -> webSocketHandler.makeMove(ctx, command);
//                        case LEAVE -> webSocketHandler.leave(ctx, command);
//                        case RESIGN -> webSocketHandler.resign(ctx, command);
//                    }
//                } catch (Exception e) {
//                    ctx.send(new Gson().toJson(new ErrorMessage("invalid command")));
//                }
//            });

            ws.onClose(ctx -> {
                System.out.println("WS closed on server");
                webSocketHandler.onClose(ctx);
            });
            ws.onError(ctx -> { });
        });

    }

    private void handleWsMessage(WsContext ctx, String message) {
        try {
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

            if (command == null || command.getCommandType() == null) {
                sendWsError(ctx, "invalid command");
                return;
            }

            switch (command.getCommandType()) {
                case CONNECT -> webSocketHandler.connect(ctx, command);
                case MAKE_MOVE -> webSocketHandler.makeMove(ctx, command);
                case LEAVE -> webSocketHandler.leave(ctx, command);
                case RESIGN -> webSocketHandler.resign(ctx, command);
            }
        } catch (Exception e) {
            sendWsError(ctx, "invalid command");
        }
    }

    private void sendWsError(WsContext ctx, String message) {
        ctx.send(new Gson().toJson(new ErrorMessage(message)));
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
