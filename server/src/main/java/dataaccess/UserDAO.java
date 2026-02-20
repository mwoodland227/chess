package dataaccess;

import dataClasses.UserData;

public interface UserDAO {
    UserData getUser(String username);

    Void createUser(UserData userData);

}
