package dataaccess;

import dataClasses.AuthData;
import dataClasses.GameData;
import dataClasses.UserData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGame implements GameDAO{
    final private HashMap<Integer, GameData> gameDatabase = new HashMap<>();


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
