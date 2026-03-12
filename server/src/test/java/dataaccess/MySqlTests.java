package dataaccess;

import dataclasses.UserData;
import dataclasses.AuthData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MySqlTests {
    private static MySqlUser userDao;
    private static MySqlGame gameDao;

    @BeforeAll
    static void setup() throws DataAccessException{
        userDao = new MySqlUser();
        gameDao = new MySqlGame();
    }

    @Test
    void clearUsersPositive() throws DataAccessException {
        userDao.createUser(new UserData("user", "password", "email@gmail"));
        userDao.createUser(new UserData("cameron", "pass", "c@m"));

        userDao.clearUsers();

        assertNull(userDao.getUser("user"));
        assertNull(userDao.getUser("cameron"));
    }

    @Test
    void clearAuthPositive() throws DataAccessException {
        userDao.createUser(new UserData("cameron", "pass", "c@m"));
        userDao.createAuth(new AuthData("token", "cam"));

        userDao.clearAuth();

        assertNull(userDao.getAuth("token"));
    }

    @Test
    void getUserPositive() throws DataAccessException {
        userDao.createUser(new UserData("cam", "pass", "c@g"));
        UserData result = userDao.getUser("cam");

        assertNotNull(result);
        assertEquals("cam", result.username());
    }

    @Test
    void getUserNegative() throws DataAccessException {
        UserData result = userDao.getUser("none");
        assertNull(result);
    }


    @Test
    void createUserPositive() throws DataAccessException{
        UserData user = new UserData("cameron", "pass", "c@g");
        userDao.createUser(user);
        UserData result = userDao.getUser("cameron");

        assertNotNull(result);
        assertEquals("cameron", result.username());
        assertNotEquals("pass", result.password());
        assertEquals("c@g", result.email());
    }

    @Test
    void createAuthPositive() throws DataAccessException{
        userDao.createUser(new UserData("cameron", "pass", "c@g"));
        AuthData auth = new AuthData("token", "cameron");

        userDao.createAuth(auth);
        AuthData result = userDao.getAuth("token");

        assertNotNull(result);
        assertEquals("cameron", result.username());
        assertEquals("token", result.authToken());
    }

    @Test
    void createUserNegative() throws DataAccessException {
        userDao.createUser(new UserData("cam", "pass", "c@g"));

        assertThrows(DataAccessException.class, () -> {
            userDao.createUser(new UserData("cam", "pass2", "cj@g"));
        });
    }


    @Test
    void createAuthNegative_duplicateToken() throws DataAccessException {
        userDao.createUser(new UserData("cam", "pass", "c@g"));
        userDao.createAuth(new AuthData("dupt", "cam"));

        assertThrows(DataAccessException.class, () -> {
            userDao.createAuth(new AuthData("dupt", "cam"));
        });
    }


    @Test
    void deleteAuthPositive() throws DataAccessException{
        userDao.createUser(new UserData("cameron", "pass", "c@g"));
        userDao.createAuth(new AuthData("token", "cameron"));

        userDao.deleteAuth("token");

        assertNull(userDao.getAuth("token"));
    }

    @Test
    void getAuthPositive() throws DataAccessException {
        userDao.createUser(new UserData("cam", "pass", "c@g"));
        userDao.createAuth(new AuthData("token", "cam"));
        AuthData result = userDao.getAuth("token");

        assertNotNull(result);
        assertEquals("cam", result.username());
    }

    @Test
    void getAuthNegative_missingAuth() throws DataAccessException {
        AuthData result = userDao.getAuth("noToken");
        assertNull(result);
    }

    @Test
    void createGamePositive() throws DataAccessException {
        int gameId = gameDao.createGame("Test Game");

        assertTrue(gameId > 0);

        GameData game = gameDao.getGame(gameId);
        assertNotNull(game);
        assertEquals("Test Game", game.gameName());
    }
}
