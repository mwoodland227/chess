package service;

import dataClasses.AuthData;
import dataClasses.UserData;

import dataaccess.UserDAO;
import handler.RegisterRequest;

import java.util.UUID;


public class Register {
    private final UserDAO userDAO;

    public Register(UserDAO userDAO) {
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


}
