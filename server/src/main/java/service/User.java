package service;

import chess.ChessGame;
import dataClasses.AuthData;
import dataClasses.GameData;
import dataClasses.UserData;

import dataaccess.UserDAO;
import handler.*;

import java.util.Collection;
import java.util.UUID;


public class User {
    private final UserDAO userDAO;

    public User(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }


    public AuthData register(RegisterRequest registerRequest) {
        userDAO.getUser(registerRequest.username());

        UserData userData = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
        userDAO.createUser(userData);

        AuthData authData = new AuthData(generateToken(), registerRequest.username());
        userDAO.createAuth(authData);

        return authData;
    }

    public AuthData login(LoginRequest loginRequest){
        userDAO.getUser(loginRequest.username());

        AuthData authData = new AuthData(generateToken(), loginRequest.username());
        userDAO.createAuth(authData);
        return authData;
    }


    public void logout(LogoutRequest logoutRequest) {
        userDAO.getAuth(logoutRequest.authToken());
        userDAO.deleteAuth(logoutRequest.authToken());

    }

    public Collection<GameData> listGames(ListGamesRequest listGamesRequest) {
        AuthData authData = userDAO.getAuth(listGamesRequest.authToken());
        return userDAO.listGames();
    }

    public int createGame(String authToken, String gameName) {
        AuthData authData = userDAO.getAuth(authToken);
        int gameID = 1;
        int id = userDAO.createGame(new GameData(gameID, null, null, gameName, new ChessGame()));
        gameID = gameID +1;

        return id;
    }
}
