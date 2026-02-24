package handler;
import com.google.gson.Gson;
import dataClasses.AuthData;
import dataaccess.UserDAO;
import io.javalin.http.Context;

import service.User;

public class Handler {
    public final User user;

    public Handler(UserDAO userDAO) {
        this.user = new User(userDAO);
    }

    public void handleRegister(Context ctx) {
        RegisterRequest registerRequest = new Gson().fromJson(ctx.body(), RegisterRequest.class);
        AuthData registerResult = user.register(registerRequest);
        ctx.result(new Gson().toJson(registerResult));
        ctx.status(200);
    }

    public void handleLogin(Context ctx) {
        LoginRequest loginRequest = new Gson().fromJson(ctx.body(), LoginRequest.class);
        AuthData loginResult = user.login(loginRequest);
        ctx.result(new Gson().toJson(loginResult));
        ctx.status(200);
    }

    public void handleLogout(Context ctx) {
        LogoutRequest logoutRequest = new LogoutRequest(ctx.header("authorization"));
        user.logout(logoutRequest);

    }
}
