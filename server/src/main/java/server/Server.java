package server;

import dataaccess.GameDAO;
import dataaccess.MemoryGame;
import dataaccess.MemoryUser;
import dataaccess.UserDAO;
import handler.Handler;
import io.javalin.*;

public class Server {
    public final Handler handle;
    public final UserDAO memoryUser;
    public final GameDAO memoryGame;

    private final Javalin javalin;

    public Server() {
        this.memoryGame = new MemoryGame();
        this.memoryUser = new MemoryUser();
        this.handle = new Handler(memoryUser,memoryGame);

        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        javalin.delete("/db", ctx -> {
            ctx.status(200).result("{}");
        });

        javalin.post("/user", handle::handleRegister);

        javalin.post("/session", handle::handleLogin);

        javalin.delete("/session", handle::handleLogout);

        javalin.get("/game", handle::handleListGames);

        javalin.post("/game", handle::handleCreateGame);

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
