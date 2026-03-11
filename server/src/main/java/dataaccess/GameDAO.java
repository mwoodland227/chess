package dataaccess;

import dataclasses.GameData;

import java.util.Collection;

public interface GameDAO {

    Collection<GameData> listGames();

    int createGame(String gameName) throws DataAccessException;

    void clearGames();

    GameData getGame(int gameID) throws DataAccessException;

    void updateGame(GameData updatedGame);
}
