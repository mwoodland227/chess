package dataaccess;

import dataclasses.GameData;

import java.util.Collection;
import java.util.List;

public class MySqlGame implements GameDAO{
    @Override
    public Collection<GameData> listGames() {
        return List.of();
    }

    @Override
    public int createGame(String gameName) {
        return 0;
    }

    @Override
    public void clearGames() {

    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public void updateGame(GameData updatedGame) {

    }
}
