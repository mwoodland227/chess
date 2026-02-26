package dataaccess;

import dataClasses.AuthData;
import dataClasses.GameData;
import dataClasses.UserData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryUser implements UserDAO {
    final private HashMap<String, UserData> userDatabase = new HashMap<>();
    final private HashMap<String, AuthData> authDatabase = new HashMap<>();
    final private HashMap<Integer, GameData> gameDatabase = new HashMap<>();

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

    @Override
    public AuthData getAuth(String authToken) {
        return authDatabase.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) {
        authDatabase.remove(authToken);
    }

    @Override
    public Collection<GameData> listGames() {
        return gameDatabase.values();
    }

    @Override
    public int createGame(GameData gameData) {
        int gameID = gameData.gameID();
        gameDatabase.put(gameID, gameData);
        return gameID;
    }

}
