package server;

import dataaccess.MemoryUser;
import dataaccess.UserDAO;
import handler.Handler;
import io.javalin.*;

public class Server {
    public final Handler handle;
    public final UserDAO memoryUser;

    private final Javalin javalin;

    public Server() {
        this.memoryUser = new MemoryUser();
        this.handle = new Handler(memoryUser);

        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        javalin.delete("/db", ctx -> {
            ctx.status(200).result("{}");
        });

        javalin.post("/user", handle::handleRegister);

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
