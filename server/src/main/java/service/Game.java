package service;
import chess.ChessGame;
import dataClasses.AuthData;
import dataClasses.GameData;
import dataClasses.UserData;

import dataaccess.GameDAO;
import dataaccess.UserDAO;
import handler.*;

import java.util.Collection;
import java.util.UUID;

public class Game {
    private final UserDAO userDAO;
    private final GameDAO gameDAO;

    public Game(UserDAO userDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
    }

    public Collection<GameData> listGames(ListGamesRequest listGamesRequest) {
        AuthData authData = userDAO.getAuth(listGamesRequest.authToken());
        if(authData == null) {
            throw new RuntimeException("Error: unauthorized");
        }

        return gameDAO.listGames();
    }


    public int createGame(String authToken, String gameName) {
        AuthData authData = userDAO.getAuth(authToken);
        if(authData == null) {
            throw new RuntimeException("Error: unauthorized");
        }

        int gameID = 1;
        int id = gameDAO.createGame(new GameData(gameID, null, null, gameName, new ChessGame()));
        gameID = gameID +1;
        return id;
    }

    public void clearGames() {
        gameDAO.clearGames();
    }

    public void joinGame(String authToken, ChessGame.TeamColor playerColor, int gameID) throws UnauthorizedException, AlreadyTakenException, BadRequestException {
        AuthData authData = userDAO.getAuth(authToken);
        if(authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        if(gameID == 0) {
            throw new BadRequestException("Error: bad request");
        }
        if(playerColor != ChessGame.TeamColor.WHITE && playerColor != ChessGame.TeamColor.BLACK) {
            throw new BadRequestException("Error: bad request");
        }

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
