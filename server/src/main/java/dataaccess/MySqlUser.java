package dataaccess;

import dataclasses.AuthData;
import dataclasses.UserData;

import java.sql.*;

public class MySqlUser implements UserDAO{
    public MySqlUser() {
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
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        DatabaseManager.executeUpdate(statement, userData.username(), userData.password(), userData.email());
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        var statement = "INSERT INTO auth (username, authToken) VALUES (?,?)";
        DatabaseManager.executeUpdate(statement, authData.username(), authData.authToken());

    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()){
            var statement = "SELECT username, authToken FROM auth WHERE authToken = ?";
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
        return new AuthData(authToken, username);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM auth WHERE authToken = ?";
        DatabaseManager.executeUpdate(statement, authToken);
    }

    @Override
    public void clearUsers() throws DataAccessException {
        var statement = "DELETE FROM user";
        DatabaseManager.executeUpdate(statement);

    }

    @Override
    public void clearAuth() throws DataAccessException {
        var statement = "DELETE FROM auth";
        DatabaseManager.executeUpdate(statement);

    }


}
