package dataaccess;

import dataClasses.AuthData;
import dataClasses.UserData;

public interface UserDAO {
    UserData getUser(String username);

    void createUser(UserData userData);

    void createAuth(AuthData authData);

    AuthData getAuth(String authToken);

    void deleteAuth(String authToken);

}
