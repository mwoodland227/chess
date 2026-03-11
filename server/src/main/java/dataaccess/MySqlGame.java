package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import dataclasses.GameData;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.sql.Types;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class MySqlGame implements GameDAO{
    public MySqlGame(){
    }
    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        var result = new ArrayList<GameData>();
        try(Connection conn = DatabaseManager.getConnection()){
            var statement = "SELECT gameID, gameName, whiteUsername, blackUsername, gameState FROM game";
            try(PreparedStatement ps = conn.prepareStatement(statement)){
                try (ResultSet rs = ps.executeQuery()){
                    while(rs.next()){
                        result.add(readGame(rs));

                    }
                }
            }
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage(), e.getErrorCode());
        }
        return result;
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        var statement = "INSERT INTO game (gameName) VALUES (?)";
        return executeUpdate(statement, gameName);
    }

    @Override
    public void clearGames() throws DataAccessException {
        var statement = "DELETE FROM game";
        executeUpdate(statement);

    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()){
            var statement = "SELECT gameName, whiteUsername, blackUsername, gameState FROM game WHERE gameID = ?";
            try(PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery()){
                    if(rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage(), e.getErrorCode());
        }
        return null;
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        int gameID = rs.getInt("gameID");
        String whiteUsername = rs.getString("whiteUsername");
        String blackUsername = rs.getString("blackUsername");
        String gameName = rs.getString("gameName");
        String gameStateJson = rs.getString("gameState");
        ChessGame game = new Gson().fromJson(gameStateJson, ChessGame.class);

        return new GameData(gameID, whiteUsername,blackUsername, gameName, game);
    }

    @Override
    public void updateGame(GameData updatedGame) throws DataAccessException {
        var statement = "UPDATE game SET gameName=?, whiteUsername=?, blackUsername=?, gameState=? WHERE gameID=?";
        String gameStateJson = new Gson().toJson(updatedGame.game());
        executeUpdate(statement, updatedGame.gameName(), updatedGame.whiteUsername(), updatedGame.blackUsername(),
                gameStateJson, updatedGame.gameID());

    }


    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()){
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)){
                for (int i = 0; i < params.length; i++){
                    Object param = params[i];
                    if(param instanceof String p){
                        ps.setString(i + 1, p);
                    }else if (param instanceof Integer p) {
                        ps.setInt(i + 1, p);
                    }else if(param == null) {
                        ps.setNull(i + 1, Types.VARCHAR);
                    }
                }
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if(rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e.getErrorCode());
        }
    }
}
