package dataaccess;

import dataclasses.UserData;
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

    void clearAuthPositive() throws DataAccessException {
        userDao.createUser(new UserData("cameron", "pass", "c@m"));

        userDao.clearUsers();

        assertNull(userDao.getUser("user"));
        assertNull(userDao.getUser("cameron"));
    }
}
