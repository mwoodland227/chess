package dataaccess;

import dataclasses.UserData;
import dataclasses.AuthData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

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
        userDao.createAuth(new AuthData("cameron", "token"));

        userDao.clearAuth();

        assertNull(userDao.getAuth("token"));
    }

    @Test
    void createUserPositive() throws DataAccessException{
        UserData user = new UserData("cameron", "pass", "c@g");
        userDao.createUser(user);
        UserData result = userDao.getUser("cameron");

        assertNotNull(result);
        assertEquals("alice", result.username());
        assertNotEquals("pass123", result.password());
        assertEquals("alice@test.com", result.email());
    }
}
