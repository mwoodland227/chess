package dataaccess;

import chess.ChessGame;
import dataClasses.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGame implements GameDAO{
    final private HashMap<Integer, GameData> gameDatabase = new HashMap<>();


    @Override
    public Collection<GameData> listGames() {
        return gameDatabase.values();
    }

    @Override
    public int createGame(String gameName) {
        int gameID = gameDatabase.size() +1;
        GameData gameData = new GameData(gameID, null, null, gameName, new ChessGame());
        gameDatabase.put(gameID, gameData);
        return gameID;
    }

    @Override
    public void clearGames() {
        gameDatabase.clear();
    }

    @Override
    public GameData getGame(int gameID) {
        return gameDatabase.get(gameID);
    }

    @Override
    public void updateGame(GameData updatedGame) {
        int id = updatedGame.gameID();
        gameDatabase.put(id, updatedGame);
    }
}
