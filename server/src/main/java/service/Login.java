package service;
import dataClasses.AuthData;
import dataClasses.UserData;

import dataaccess.UserDAO;
import handler.LoginRequest;

public class Login {
    private final UserDAO userDAO;

    public Login(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public AuthData login(LoginRequest loginRequest) {
        userDAO.getUser(loginRequest.username());

    }
}
