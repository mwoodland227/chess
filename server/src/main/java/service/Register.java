package service;

import dataaccess.UserDAO;
import handler.RegisterRequest;
import handler.RegisterResult;

public class Register {
    private final UserDAO userDAO;

    public Register(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public RegisterResult register(RegisterRequest registerRequest) {
        userDAO.getUser(registerRequest.username());
    }


}
