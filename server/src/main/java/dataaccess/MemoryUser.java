package dataaccess;

import dataClasses.UserData;

import java.util.HashMap;

public class MemoryUser implements UserDAO {
    final private HashMap<String, UserData> userDatabase = new HashMap<>();

    @Override
    public UserData getUser(String username) {
        return userDatabase.get(username);
    }

    @Override
    public Void createUser(UserData userData) {
        userDatabase.put(userData.username(), userData);
    }
}
