package dataaccess;

import dataClasses.AuthData;
import dataClasses.GameData;
import dataClasses.UserData;

import java.util.Collection;

public interface UserDAO {
    UserData getUser(String username);

    void createUser(UserData userData);

    void createAuth(AuthData authData);

    AuthData getAuth(String authToken);

    void deleteAuth(String authToken);

//    Collection<GameData> listGames();
//
//    int createGame(GameData gameData);
}
