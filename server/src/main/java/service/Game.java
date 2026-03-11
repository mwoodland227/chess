package service;
import chess.ChessGame;
import dataclasses.AuthData;
import dataclasses.GameData;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import handler.*;

import java.util.Collection;

public class Game {
    private final UserDAO userDAO;
    private final GameDAO gameDAO;

    public Game(UserDAO userDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
    }

    public Collection<GameData> listGames(ListGamesRequest listGamesRequest) throws DataAccessException {
        AuthData authData = userDAO.getAuth(listGamesRequest.authToken());
        if(authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        return gameDAO.listGames();
    }


    public int createGame(String authToken, String gameName) throws DataAccessException {
        AuthData authData = userDAO.getAuth(authToken);
        if(authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        if(gameName == null){
            throw new BadRequestException("Error: bad request");
        }

        return gameDAO.createGame(gameName);
    }

    public void clearGames() throws DataAccessException {
        gameDAO.clearGames();
    }

    public void joinGame(String authToken, String color, int gameID) throws DataAccessException {
        AuthData authData = userDAO.getAuth(authToken);
        if(authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        if(gameID == 0) {
            throw new BadRequestException("Error: bad request");
        }
        if(color == null || !color.equals("WHITE") && !color.equals("BLACK")) {
            throw new BadRequestException("Error: bad request");
        }
        ChessGame.TeamColor playerColor = ChessGame.TeamColor.valueOf(color);
        GameData gameData = gameDAO.getGame(gameID);

        if(gameData.gameName() == null) {
            throw new BadRequestException("Error: bad request");
        }
        String username = authData.username();
        // check the color they want is null to update the game
        GameData updatedGame;
        if(playerColor == ChessGame.TeamColor.WHITE && gameData.whiteUsername() == null) {
            String blackUsername = gameData.blackUsername();
            updatedGame = new GameData(gameData.gameID(), username, blackUsername, gameData.gameName(), gameData.game());
        } else if (playerColor == ChessGame.TeamColor.BLACK && gameData.blackUsername() == null) {
            String whiteUsername = gameData.whiteUsername();
            updatedGame = new GameData(gameData.gameID(), whiteUsername, username, gameData.gameName(), gameData.game());

        } else {
            throw new AlreadyTakenException("Error: already taken");
        }

        gameDAO.updateGame(updatedGame);

    }
}
