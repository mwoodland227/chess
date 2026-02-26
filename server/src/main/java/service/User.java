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


    public void clearUsers() {
        userDAO.clearUsers();
    }

    public void clearAuth() {
        userDAO.clearAuth();
    }
}
