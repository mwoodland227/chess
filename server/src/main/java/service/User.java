package service;

import dataclasses.AuthData;
import dataclasses.UserData;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import handler.*;
import org.mindrot.jbcrypt.BCrypt;

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


    public AuthData register(RegisterRequest registerRequest) throws DataAccessException {
        if(registerRequest.username() == null || registerRequest.email() == null || registerRequest.password() == null){
            throw new BadRequestException("bad request");
        }
        if(userDAO.getUser(registerRequest.username()) == null) {
            String hashedPassword = BCrypt.hashpw(registerRequest.password(), BCrypt.gensalt());

            UserData userData = new UserData(registerRequest.username(), hashedPassword, registerRequest.email());
            userDAO.createUser(userData);

            AuthData authData = new AuthData(generateToken(), registerRequest.username());
            userDAO.createAuth(authData);

            return authData;
        } else {
            throw new AlreadyTakenException("already taken");
        }
    }

    public AuthData login(LoginRequest loginRequest) throws DataAccessException {
        if(loginRequest.username() == null || loginRequest.password() == null){
            throw new BadRequestException("bad request");
        }

        UserData userData = userDAO.getUser(loginRequest.username());
        if( userData == null) {
            throw new UnauthorizedException("unauthorized");

        }

        if(!BCrypt.checkpw(loginRequest.password(), userData.password())) {
            throw new UnauthorizedException("unauthorized");
        }

        AuthData authData = new AuthData(generateToken(), loginRequest.username());
        userDAO.createAuth(authData);
        return authData;
    }


    public void logout(LogoutRequest logoutRequest) throws DataAccessException {
        AuthData auth = userDAO.getAuth(logoutRequest.authToken());
        if(auth == null) {
            throw new UnauthorizedException("unauthorized");
        }
        userDAO.deleteAuth(logoutRequest.authToken());

    }


    public void clearUsers() throws DataAccessException {
        userDAO.clearUsers();
    }

    public void clearAuth() throws DataAccessException {
        userDAO.clearAuth();
    }
}
