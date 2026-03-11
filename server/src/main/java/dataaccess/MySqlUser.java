package dataaccess;

import dataclasses.AuthData;
import dataclasses.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class MySqlUser implements UserDAO{
    public MySqlUser() throws DataAccessException {
        configureDatabase();
    }
    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public void createUser(UserData userData) {
        String clearTextPassword = userData.password();
        String hashedPassword = BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        executeUpdate(statement, userData.username(), hashedPassword, userData.email());
    }

    @Override
    public void createAuth(AuthData authData) {

    }

    @Override
    public AuthData getAuth(String authToken) {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) {

    }

    @Override
    public void clearUsers() {

    }

    @Override
    public void clearAuth() {

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
                throw new DataAccessException();
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
            throw new RuntimeException(e);
        }
    }
}
