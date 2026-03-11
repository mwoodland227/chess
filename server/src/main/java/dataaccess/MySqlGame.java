package dataaccess;

import dataclasses.GameData;

import java.sql.*;
import java.util.Collection;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class MySqlGame implements GameDAO{
    public MySqlGame() throws DataAccessException{
        configureDatabase();
    }
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

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS game (
             `id` int NOT NULL AUTO_INCREMENT,
             `gameName` varchar(256) DEFAULT NOT NULL,
             `whiteUsername` varchar(256) DEFAULT NULL,
             `blackUsername` varchar(256) DEFAULT NULL,
             PRIMARY KEY (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try(Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        }catch(SQLException ex){
            throw new DataAccessException(ex.getMessage(), ex.getErrorCode());
        }

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
