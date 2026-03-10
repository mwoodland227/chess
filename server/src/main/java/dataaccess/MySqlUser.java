package dataaccess;

import dataclasses.AuthData;
import dataclasses.UserData;

import java.sql.Connection;
import java.sql.SQLException;

public class MySqlUser implements UserDAO{
    public MySqlUser() {
        configureDatabase();
    }
    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public void createUser(UserData userData) {

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
            CREATE TABLE IS NOT EXISTS user(
            'id' int NOT NULL AUTO_INCREMENT,
            'username' varchar(256) NOT NULL,
            'password' varchar(256) NOT NULL,
            'email' varchar(256) NOT NULL,
            PRIMARY KEY('id'),
            UNIQUE KEY 'username' ('username'),
            UNIQUE KEY 'email' ('email')
            )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        try(Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatments) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        }catch(SQLException ex){
                throw new DataAccessException();
        }

    }
}
