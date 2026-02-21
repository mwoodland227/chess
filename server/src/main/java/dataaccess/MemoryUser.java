package dataaccess;

import dataClasses.AuthData;
import dataClasses.UserData;

import java.util.HashMap;

public class MemoryUser implements UserDAO {
    final private HashMap<String, UserData> userDatabase = new HashMap<>();
    final private HashMap<String, AuthData> authDatabase = new HashMap<>();

    @Override
    public UserData getUser(String username) {
        return userDatabase.get(username);
    }

    @Override
    public void createUser(UserData userData) {
        userDatabase.put(userData.username(), userData);
    }

    @Override
    public void createAuth(AuthData authData) {
        authDatabase.put(authData.username(), authData);
    }
}
