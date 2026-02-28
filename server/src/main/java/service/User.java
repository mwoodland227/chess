package service;

import chess.ChessGame;
import dataClasses.AuthData;
import dataClasses.GameData;
import dataClasses.UserData;

import dataaccess.UserDAO;
import handler.*;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;


public class User {
    private final UserDAO userDAO;


    public User(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }


    public AuthData register(RegisterRequest registerRequest) throws BadRequestException, AlreadyTakenException {
        if(registerRequest.username() == null || registerRequest.email() == null || registerRequest.password() == null){
            throw new BadRequestException("Error: bad request");
        }
        if(userDAO.getUser(registerRequest.username()) == null) {

            UserData userData = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
            userDAO.createUser(userData);

            AuthData authData = new AuthData(generateToken(), registerRequest.username());
            userDAO.createAuth(authData);

            return authData;
        } else {
            throw new AlreadyTakenException("Error: already taken");
        }
    }

    public AuthData login(LoginRequest loginRequest) throws BadRequestException, UnauthorizedException {
        if(loginRequest.username() == null || loginRequest.password() == null){
            throw new BadRequestException("Error: bad request");
        }

        UserData userData = userDAO.getUser(loginRequest.username());
        if( userData == null) {
            throw new UnauthorizedException("Error: unauthorized");

        }

        if(!Objects.equals(userData.password(), loginRequest.password())) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        AuthData authData = new AuthData(generateToken(), loginRequest.username());
        userDAO.createAuth(authData);
        return authData;
    }


    public void logout(LogoutRequest logoutRequest) throws UnauthorizedException {
        AuthData auth = userDAO.getAuth(logoutRequest.authToken());
        if(auth == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        userDAO.deleteAuth(logoutRequest.authToken());

    }


    public void clearUsers() {
        userDAO.clearUsers();
    }

    public void clearAuth() {
        userDAO.clearAuth();
    }
}
