package service;
import dataClasses.AuthData;
import dataaccess.*;
import handler.CreateGameRequest;
import handler.LoginRequest;
import handler.LogoutRequest;
import handler.RegisterRequest;
import org.junit.jupiter.api.*;
import passoff.model.*;

import java.util.*;
import org.junit.jupiter.api.*;


import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {
    User user;
    Game game;
    UserDAO userDAO;
    GameDAO gameDAO;

    @AfterAll
    static void stopServer() {

    }

    @BeforeAll
    public static void init() {

    }

    @BeforeEach
    public void setup() {
        userDAO = new MemoryUser();
        gameDAO = new MemoryGame();

        user = new User(userDAO);
        game = new Game(userDAO, gameDAO);

    }

    // ### SERVER-LEVEL API TESTS ###

    @Test
    @DisplayName("Register Request Positive")
    public void registerRequestSuccess() throws DataAccessException {
        RegisterRequest req = new RegisterRequest("cameron", "pass", "c@g.com");
        AuthData auth = user.register(req);

        assertNotNull(auth);
        assertEquals("cameron", auth.username());
        assertNotNull(userDAO.getUser("cameron"));
        assertNotNull(userDAO.getAuth(auth.authToken()));
    }

    @Test
    @DisplayName("Register Request Negative")
    public void registerRequestFail() {
        RegisterRequest req = new RegisterRequest("cameron", "pass", null);

        assertThrows(DataAccessException.class, ()-> user.register(req));
    }

    @Test
    @DisplayName("Login Request Positive")
    public void loginRequestSuccess() throws DataAccessException {
        RegisterRequest req = new RegisterRequest("cameron", "pass", "c@g.com");
        user.register(req);

        LoginRequest login = new LoginRequest("cameron", "pass");
        AuthData auth = user.login(login);

        assertNotNull(auth);
        assertEquals("cameron", auth.username());
        assertNotNull(userDAO.getUser("cameron"));
        assertNotNull(userDAO.getAuth(auth.authToken()));
    }

    @Test
    @DisplayName("Login Request Negative")
    public void loginRequestFail() {
        LoginRequest login = new LoginRequest("cameron", null);
        assertThrows(DataAccessException.class, ()-> user.login(login));
    }


    @Test
    @DisplayName("Logout Request Positive")
    public void logoutRequestSuccess() throws DataAccessException {
        RegisterRequest req = new RegisterRequest("cameron", "pass", "c@g.com");
        user.register(req);

        LoginRequest login = new LoginRequest("cameron", "pass");
        AuthData auth = user.login(login);

        LogoutRequest logout = new LogoutRequest(auth.authToken());
        user.logout(logout);

        assertNull(userDAO.getAuth(auth.authToken()));
    }

    @Test
    @DisplayName("Logout Request Negative")
    public void logoutRequestFail() {
        LogoutRequest req = new LogoutRequest("cams");
        assertThrows(DataAccessException.class, ()-> user.logout(req));
    }

    @Test
    @DisplayName("Clear Users Positive")
    public void clearUsersSuccess() throws DataAccessException {
        RegisterRequest req1 = new RegisterRequest("cameron", "pass", "c@g.com");
        RegisterRequest req2 = new RegisterRequest("cams", "pass", "c@g.com");
        user.register(req1);
        user.register(req2);

        assertNotNull(userDAO.getUser("cameron"));
        assertNotNull(userDAO.getUser("cams"));
        user.clearUsers();
        assertNull(userDAO.getUser("cameron"));
        assertNull(userDAO.getUser("cams"));
    }

    @Test
    @DisplayName("Clear Auth Positive")
    public void clearAuthSuccess() throws DataAccessException {
        RegisterRequest req1 = new RegisterRequest("cameron", "pass", "c@g.com");
        RegisterRequest req2 = new RegisterRequest("cams", "pass", "c@g.com");
        user.register(req1);
        user.register(req2);

        LoginRequest login = new LoginRequest("cameron", "pass");
        AuthData auth = user.login(login);

        LoginRequest login2 = new LoginRequest("cams", "pass");
        AuthData auth2 = user.login(login2);


        assertNotNull(userDAO.getAuth(auth.authToken()));
        assertNotNull(userDAO.getAuth(auth2.authToken()));
        user.clearAuth();
        assertNull(userDAO.getAuth(auth.authToken()));
        assertNull(userDAO.getAuth(auth2.authToken()));;
    }


    @Test
    @DisplayName("Create Game Positive")
    public void createGameSuccess() throws DataAccessException{
        RegisterRequest req = new RegisterRequest("cameron", "pass", "c@g.com");
        user.register(req);

        LoginRequest login = new LoginRequest("cameron", "pass");
        AuthData auth = user.login(login);

        CreateGameRequest newGame = new CreateGameRequest("Game1");


        assertNotNull(newGame.gameName());
        int gameID = game.createGame(auth.authToken(), newGame.gameName());
        assertNotNull(gameDAO.getGame(gameID));
    }

    @Test
    @DisplayName("Create Game Negative")
    public void createGameFail(){
        CreateGameRequest newGame = new CreateGameRequest("Game1");
        assertThrows(DataAccessException.class, ()-> game.createGame("cams", newGame.gameName()));
    }
}
