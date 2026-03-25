package handler;
import dataclasses.GameData;

import java.util.List;

public class ListGamesResponse {
    private List<GameData> games;

    public List<GameData> games(){
        return games;
    }
}
