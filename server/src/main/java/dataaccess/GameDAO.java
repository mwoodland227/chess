package dataaccess;

import dataClasses.GameData;

import java.util.Collection;

public interface GameDAO {

    Collection<GameData> listGames();

    int createGame(String gameName);

    void clearGames();

    GameData getGame(int gameID);

    void updateGame(GameData updatedGame);
}
