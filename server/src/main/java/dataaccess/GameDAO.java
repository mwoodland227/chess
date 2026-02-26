package dataaccess;

import dataClasses.GameData;

import java.util.Collection;

public interface GameDAO {

    Collection<GameData> listGames();

    int createGame(GameData gameData);

    void clearGames();

    GameData getGame(int gameID);

    GameData updateGame(GameData gameData, String playerColor);
}
