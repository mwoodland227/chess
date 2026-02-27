package handler;
import chess.ChessGame;
import com.google.gson.Gson;
import dataClasses.AuthData;
import dataClasses.GameData;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import io.javalin.http.Context;

import org.jetbrains.annotations.NotNull;
import service.Game;
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
        ctx.result("{}");
        ctx.status(200);
    }

    public void handleListGames(Context ctx) {
        ListGamesRequest listGamesRequest = new ListGamesRequest(ctx.header("authorization"));
        Collection<GameData> listGamesResult = game.listGames(listGamesRequest);

        ctx.result(new Gson().toJson(Map.of("games", listGamesResult)));
        ctx.status(200);
    }

    public void handleCreateGame(Context ctx) {
        String authToken = ctx.header("authorization");
        CreateGameRequest createGameRequest = new Gson().fromJson(ctx.body(), CreateGameRequest.class);
        int id = game.createGame(authToken, createGameRequest.gameName());

        ctx.result(new Gson().toJson(Map.of("gameID", id)));
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
        ChessGame.TeamColor playerColor = ChessGame.TeamColor.valueOf(joinGameRequest.playerColor());
        game.joinGame(authToken, playerColor, joinGameRequest.gameID());
        ctx.result("{}");
        ctx.status(200);
    }
}
