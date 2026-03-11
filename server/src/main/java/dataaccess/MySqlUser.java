package dataaccess;

import dataclasses.AuthData;
import dataclasses.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.sql.Types;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

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
        return new AuthData(username, authToken);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM auth WHERE authToken = ?";
        executeUpdate(statement, authToken);
    }

    @Override
    public void clearUsers() throws DataAccessException {
        var statement = "DELETE FROM user";
        executeUpdate(statement);

    }

    @Override
    public void clearAuth() throws DataAccessException {
        var statement = "DELETE FROM auth";
        executeUpdate(statement);

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
