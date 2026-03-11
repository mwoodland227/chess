package dataaccess;

import dataclasses.GameData;

import java.util.Collection;

public interface GameDAO {

    Collection<GameData> listGames() throws DataAccessException;

    int createGame(String gameName) throws DataAccessException;

    void clearGames() throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    void updateGame(GameData updatedGame) throws DataAccessException;
}
