package handler;
import com.google.gson.Gson;
import dataClasses.AuthData;
import dataClasses.GameData;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import io.javalin.http.Context;


import service.Game;
import service.UnauthorizedException;
import service.User;

import java.util.Collection;
import java.util.Map;

public class Handler {
    public final User user;
    public final Game game;

    public Handler(UserDAO userDAO, GameDAO gameDAO) {
        this.user = new User(userDAO);
        this.game = new Game(userDAO, gameDAO);
    }

    public void handleRegister(Context ctx) {
        RegisterRequest registerRequest = new Gson().fromJson(ctx.body(), RegisterRequest.class);

        try {
            AuthData registerResult = user.register(registerRequest);
            ctx.result(new Gson().toJson(registerResult));
            ctx.status(200);
        }catch (DataAccessException e) {
            ctx.status(e.code());
            String message = e.getMessage();
            ctx.result(new Gson().toJson(Map.of("message", message)));
        }
    }

    public void handleLogin(Context ctx) {
        LoginRequest loginRequest = new Gson().fromJson(ctx.body(), LoginRequest.class);

        try {
            AuthData loginResult = user.login(loginRequest);
            ctx.result(new Gson().toJson(loginResult));
            ctx.status(200);
        }catch (DataAccessException e) {
            ctx.status(e.code());
            String message = e.getMessage();
            ctx.result(new Gson().toJson(Map.of("message", message)));
        }
    }

    public void handleLogout(Context ctx) {
        LogoutRequest logoutRequest = new LogoutRequest(ctx.header("authorization"));
        try {
            user.logout(logoutRequest);
            ctx.result("{}");
            ctx.status(200);
        } catch (UnauthorizedException e) {
            ctx.status(e.code());
            String message = e.getMessage();
            ctx.result(new Gson().toJson(Map.of("message", message)));
        }

    }

    public void handleListGames(Context ctx) {
        ListGamesRequest listGamesRequest = new ListGamesRequest(ctx.header("authorization"));

        try {
            Collection<GameData> gameList = game.listGames(listGamesRequest);

            ctx.result(new Gson().toJson(Map.of("games", gameList)));
            ctx.status(200);
        }catch (DataAccessException e) {
            ctx.status(e.code());
            String message = e.getMessage();
            ctx.result(new Gson().toJson(Map.of("message", message)));
        }
    }

    public void handleCreateGame(Context ctx) {
        String authToken = ctx.header("authorization");
        CreateGameRequest createGameRequest = new Gson().fromJson(ctx.body(), CreateGameRequest.class);

        try {
            int id = game.createGame(authToken, createGameRequest.gameName());

            ctx.result(new Gson().toJson(Map.of("gameID", id)));
        }catch (DataAccessException e) {
            ctx.status(e.code());
            String message = e.getMessage();
            ctx.result(new Gson().toJson(Map.of("message", message)));
        }
    }

    public void handleClear(Context ctx) {
        user.clearUsers();
        user.clearAuth();
        game.clearGames();
        ctx.result("{}");
        ctx.status(200);
    }

    public void handleJoinGame(Context ctx) {
        String authToken = ctx.header("authorization");
        JoinGameRequest joinGameRequest = new Gson().fromJson(ctx.body(), JoinGameRequest.class);

        try {
            game.joinGame(authToken, joinGameRequest.playerColor(), joinGameRequest.gameID());
            ctx.result("{}");
            ctx.status(200);
        }catch (DataAccessException e) {
            ctx.status(e.code());
            String message = e.getMessage();
            ctx.result(new Gson().toJson(Map.of("message", message)));
        }

    }
}
