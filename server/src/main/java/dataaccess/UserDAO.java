package dataaccess;

import dataclasses.AuthData;
import dataclasses.UserData;


public interface UserDAO {
    UserData getUser(String username) throws DataAccessException;

    void createUser(UserData userData) throws DataAccessException;

    void createAuth(AuthData authData) throws DataAccessException;

    AuthData getAuth(String authToken) ;

    void deleteAuth(String authToken);

    void clearUsers();

    void clearAuth();

}
