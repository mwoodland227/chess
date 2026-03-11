package server;

import dataaccess.*;
import handler.Handler;
import io.javalin.*;

public class Server {
    public final Handler handle;
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

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
