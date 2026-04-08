package server;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import dataclasses.AuthData;
import dataclasses.GameData;
import io.javalin.websocket.WsContext;
//import jakarta.websocket.*;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketHandler {
    private final UserDAO userDAO;
    private final GameDAO gameDAO;
    private final Map<Integer, Set<WsContext>> gameConnections = new ConcurrentHashMap<>();
    private final Map<WsContext, Integer> sessionToGame = new ConcurrentHashMap<>();
    private final Map<WsContext, String> sessionToUser = new ConcurrentHashMap<>();
    private record AuthGameData(String username, GameData gameData) {}

    public WebSocketHandler(UserDAO userDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
    }

    public void connect(WsContext ctx, UserGameCommand command) throws DataAccessException {
        AuthGameData data = getAuthGameData(ctx, command);
        if(data == null){
            return;
        }

        int gameID = command.getGameID();
        String username = data.username();
        GameData gameData = gameDAO.getGame(command.getGameID());
        gameConnections.computeIfAbsent(gameID, id -> ConcurrentHashMap.newKeySet()).add(ctx);
        sessionToGame.put(ctx, gameID);
        sessionToUser.put(ctx, username);

        // debugging
//        LoadGameMessage msg = new LoadGameMessage(gameData.game());
//        System.out.println("SERVER GAME NULL? " + (gameData.game() == null));
//        System.out.println("SERVER JSON: " + new Gson().toJson(msg, msg.getClass()));
//        ctx.send(new Gson().toJson(msg, msg.getClass()));

        ctx.send(new Gson().toJson(new LoadGameMessage(gameData.game())));
        //taken out for debugging

        String role;
        if(username.equals(gameData.whiteUsername())){
            role = "white";
        } else if(username.equals(gameData.blackUsername())){
            role = "black";
        } else {
            role = "observer";
        }

        broadcastToOthers(gameID, ctx, new NotificationMessage(username + " connected as " + role));
    }

    public void makeMove(WsContext ctx, UserGameCommand command) {
        AuthGameData data = getAuthGameData(ctx, command);
        if (data == null) {
            return;
        }

        String username = data.username();
        GameData gameData = data.gameData();

        boolean isWhite = username.equals(gameData.whiteUsername());
        boolean isBlack = username.equals(gameData.blackUsername());

        if(!isBlack && !isWhite){
            sendError(ctx, "observers cannot make moves");
            return;
        }

        ChessMove move = command.getMove();
        if(move == null){
            sendError(ctx, "missing move");
            return;
        }

        ChessGame game = gameData.game();
        ChessPiece piece = game.getBoard().getPiece(move.getStartPosition());
        if(piece == null){
            sendError(ctx, "invalid move");
            return;
        }

        if (isWhite && piece.getTeamColor() != ChessGame.TeamColor.WHITE) {
            sendError(ctx, "not your piece");
            return;
        }

        if (isBlack && piece.getTeamColor() != ChessGame.TeamColor.BLACK) {
            sendError(ctx, "not your piece");
            return;
        }

        try{
            game.makeMove(move);
            GameData updatedGame = new GameData(
                    gameData.gameID(),
                    gameData.whiteUsername(),
                    gameData.blackUsername(),
                    gameData.gameName(),
                    game
            );
            gameDAO.updateGame(updatedGame);
            broadcastToAll(command.getGameID(), new LoadGameMessage(updatedGame.game()));
            broadcastToOthersByUser(command.getGameID(), ctx, new NotificationMessage(username + " moved " + move));

            ChessGame.TeamColor turn = game.getTeamTurn();
            if(game.isInCheckmate(turn)){
                broadcastToAll(command.getGameID(), new NotificationMessage(turn + " is in checkmate"));
            } else if(game.isInStalemate(turn)){
                broadcastToAll(command.getGameID(), new NotificationMessage("stalemate"));
            } else if (game.isInCheck(turn)){
                broadcastToAll(command.getGameID(), new NotificationMessage(turn + " is in check"));
            }
        } catch (Exception e) {
            sendError(ctx, "server error");
        }

    }

    public void leave(WsContext ctx, UserGameCommand command) {
        AuthGameData data = getAuthGameData(ctx, command);
        if (data == null) {
            removeConnection(ctx);
            return;
        }

        String username = data.username();
        GameData gameData = data.gameData();
        int gameID = command.getGameID();

        String white = gameData.whiteUsername();
        String black = gameData.blackUsername();

        if (username.equals(white)) {
            white = null;
        } else if (username.equals(black)) {
            black = null;
        }

        try {
            GameData updatedGame = new GameData(
                    gameData.gameID(),
                    white,
                    black,
                    gameData.gameName(),
                    gameData.game()
            );
            gameDAO.updateGame(updatedGame);
        } catch (DataAccessException e) {
            sendError(ctx, "server error");
            return;
        }

        removeConnection(ctx);
        broadcastToOthers(gameID, ctx, new NotificationMessage(username + " left the game"));
    }



    public void resign(WsContext ctx, UserGameCommand command) {

        AuthGameData data = getAuthGameData(ctx, command);
        if (data == null) {
            return;
        }

        String username = data.username();
        GameData gameData = data.gameData();

        boolean isPlayer = username.equals(gameData.whiteUsername()) ||
                username.equals(gameData.blackUsername());

        if (!isPlayer) {
            sendError(ctx, "observers cannot resign");
            return;
        }

        ChessGame game = gameData.game();
        if (game.isGameOver()) {
            sendError(ctx, "game already over");
            return;
        }

        game.setGameOver(true);

        try {
            GameData updatedGame = new GameData(
                    gameData.gameID(),
                    gameData.whiteUsername(),
                    gameData.blackUsername(),
                    gameData.gameName(),
                    game
            );
            gameDAO.updateGame(updatedGame);

            broadcastToAll(command.getGameID(),
                    new NotificationMessage(username + " resigned"));
        } catch (DataAccessException e) {
            sendError(ctx, "server error");
        }
    }

    private AuthGameData getAuthGameData(WsContext ctx, UserGameCommand command) {
        try {
            AuthData auth = userDAO.getAuth(command.getAuthToken());
            if (auth == null) {
                sendError(ctx, "unauthorized");
                return null;
            }

            GameData gameData = gameDAO.getGame(command.getGameID());
            if (gameData == null) {
                sendError(ctx, "game not found");
                return null;
            }

            return new AuthGameData(auth.username(), gameData);
        } catch (DataAccessException e) {
            sendError(ctx, "server error");
            return null;
        }
    }


    private void broadcastToAll(int gameID, ServerMessage message) {
        broadcast(gameID, message, null);
    }

    private void broadcast(int gameID, ServerMessage message, WsContext excluded){
        Set<WsContext> sessions = gameConnections.get(gameID);
        if(sessions == null){
            return;
        }

        String json = new Gson().toJson(message, message.getClass());
        sessions.removeIf(session -> !session.session.isOpen());

        for(WsContext session : sessions){
            if(excluded == null || !session.equals(excluded)){
                session.send(json);
            }
        }
    }

    private void broadcastToOthers(int gameID, WsContext excluded, ServerMessage message){
        broadcast(gameID, message, excluded);
    }

    private void broadcastToOthersByUser(int gameID, WsContext excluded, ServerMessage message) {
        Set<WsContext> sessions = gameConnections.get(gameID);
        if (sessions == null) {
            return;
        }

        String excludedUser = sessionToUser.get(excluded);
        String json = new Gson().toJson(message, message.getClass());
        sessions.removeIf(session -> !session.session.isOpen());

        for (WsContext session : sessions) {
            String sessionUser = sessionToUser.get(session);
            if (excludedUser == null || !excludedUser.equals(sessionUser)) {
                session.send(json);
            }
        }
    }


    private void sendError(WsContext ctx, String message) {
        ctx.send(new Gson().toJson(new ErrorMessage(message)));
    }


    private Integer removeConnection(WsContext ctx) {
        Integer gameID = sessionToGame.remove(ctx);
        sessionToUser.remove(ctx);

        if (gameID != null) {
            Set<WsContext> sessions = gameConnections.get(gameID);
            if (sessions != null) {
                sessions.remove(ctx);
                if (sessions.isEmpty()) {
                    gameConnections.remove(gameID);
                }
            }
        }
        return gameID;
    }

    public void onClose(WsContext ctx){
        removeConnection(ctx);
    }

}
