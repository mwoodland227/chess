package dataaccess;

import dataclasses.AuthData;
import dataclasses.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.sql.Types;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class MySqlUser implements UserDAO{
    public MySqlUser() throws DataAccessException {
        configureDatabase();
    }
    @Override
    public UserData getUser(String username) throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()){
            var statement = "SELECT username, password, email FROM user WHERE username = ?";
            try(PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setString(1, username);
                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next()){
                        return readUser(rs);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e.getErrorCode());
        }
        return null;
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        String username = rs.getString("username");
        String password = rs.getString("password");
        String email = rs.getString("email");
        return new UserData(username, password, email);
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        String clearTextPassword = userData.password();
        String hashedPassword = BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        executeUpdate(statement, userData.username(), hashedPassword, userData.email());
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        var statement = "INSERT INTO auth (username, authToken) VALUES (?,?)";
        executeUpdate(statement, authData.username(), authData.authToken());

    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()){
            var statement = "SELECTION username, authToken FROM auth WHERE authToken = ?";
            try(PreparedStatement ps =conn.prepareStatement(statement)){
                ps.setString(1, authToken);
                try(ResultSet rs = ps.executeQuery()) {
                    if(rs.next()){
                        return readAuth(rs);
                    }
                }
            }
        } catch(SQLException e){
            throw new DataAccessException(e.getMessage(), e.getErrorCode());
        }
        return null;
    }

    private AuthData readAuth(ResultSet rs) throws SQLException {
        String username = rs.getString("username");
        String authToken = rs.getString("authToken");
        return new AuthData(username, authToken);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM auth WHERE authToken = ?";
        executeUpdate(statement, authToken);
    }

    @Override
    public void clearUsers() throws DataAccessException {
        var statement = "TRUNCATE TABLE user";
        executeUpdate(statement);

    }

    @Override
    public void clearAuth() throws DataAccessException {
        var statement = "TRUNCATE TABLE auth";
        executeUpdate(statement);

    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS user(
             `id` int NOT NULL AUTO_INCREMENT,
             `username` varchar(256) NOT NULL,
             `password` varchar(256) NOT NULL,
             `email` varchar(256) NOT NULL,
             PRIMARY KEY (`id`),
             UNIQUE KEY `username` (`username`),
             UNIQUE KEY `email` (`email`)
            )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,

            """
            CREATE TABLE IF NOT EXISTS auth (
              `id` int NOT NULL AUTO_INCREMENT,
              `username` varchar(256) NOT NULL,
              `authToken` varchar(256) NOT NULL,
              PRIMARY KEY (`id`),
              INDEX (`username`)
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
