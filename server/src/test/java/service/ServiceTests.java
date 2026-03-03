package service;
import chess.ChessGame;
import dataClasses.AuthData;
import dataaccess.*;
import handler.LoginRequest;
import handler.LogoutRequest;
import handler.RegisterRequest;
import org.junit.jupiter.api.*;
import passoff.model.*;
import passoff.server.TestServerFacade;
import server.Server;

import java.net.HttpURLConnection;
import java.util.*;
import org.junit.jupiter.api.*;
import passoff.model.TestAuthResult;
import passoff.model.TestCreateRequest;
import passoff.model.TestUser;
import passoff.server.TestServerFacade;
import server.Server;

import java.net.HttpURLConnection;

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
    public void registerRequestFail() throws DataAccessException {
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
    public void loginRequestFail() throws DataAccessException{
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
    public void logoutRequestFail() throws DataAccessException{
        LogoutRequest req = new LogoutRequest("cams");
        assertThrows(DataAccessException.class, ()-> user.logout(req));
    }


}
