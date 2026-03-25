package client;

import dataclasses.AuthData;
import dataclasses.GameData;
import dataclasses.UserData;
import org.junit.jupiter.api.*;
import server.Server;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private static int port;

    @BeforeAll
    public static void init() {
        server = new Server();
        port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }


    @AfterAll
    static void stopServer() {
        server.stop();
    }


//    @BeforeEach
//    void clearData(){
//        try{
//            facade.register("cleanTest", "cleanPass", "clean@g");
//        } catch(Exception ignored){}
//    }

    @Test
    void registerPositive() throws ClientException{
        UserData user = facade.register("cams", "pass", "c@g");
        assertNotNull(user);
        assertEquals("cams", user.username());
    }

    @Test
    void registerNegative() {
        assertThrows(ClientException.class, () -> {
            facade.register("camsR", "pass", "c@g");
            facade.register("camsR", "pass1", "c2@g");
        });

    }


    @Test
    void loginPositive() throws ClientException{
        facade.register("login", "pass", "c@g");
        AuthData auth = facade.login("login", "pass");

        assertNotNull(auth.authToken());
    }

    @Test
    void loginNegative(){
        assertThrows(ClientException.class, () ->
                facade.login("loginBad", "bad"));
    }

    @Test
    void logoutPositive() throws ClientException {
        facade.register("logout1", "pass", "l@g");
        var auth = facade.login("logout1", "pass");
        facade.logout(auth.authToken());
    }

    @Test
    void logoutNegative(){
        assertThrows(ClientException.class, () ->
                facade.logout("fake"));
    }

    @Test
    void createGamePositive() throws ClientException {
        facade.register("create", "pass", "c@g");
        var auth = facade.login("create", "pass");
        GameData game = facade.createGame(auth.authToken(), "testGame");
        assertEquals("testGame", game.gameName());
        assertTrue(game.gameID() > 0);
    }

    @Test
    void createGameNegative() {
        assertThrows(ClientException.class, () -> facade.createGame("none", "fake"));
    }

    @Test
    void listGamePositive() throws ClientException{
        facade.register("list1", "pass", "list@g");
        var auth = facade.login("list", "pass");
        facade.createGame(auth.authToken(), "listGame");
        var games = facade.listGames(auth.authToken());
        assertFalse(games.isEmpty());
    }

    @Test
    void listGamesNegative(){
        assertThrows(ClientException.class, () -> facade.listGames("fake"));
    }

    @Test
    void joinGamePositive() throws ClientException{
        facade.register("join", "pass", "joing@g");
        var auth = facade.login("join", "pass");
        GameData game = facade.createGame(auth.authToken(), "joinGame");
        facade.joinGame(auth.authToken(), game.gameID(), "WHITE");
        var games = facade.listGames(auth.authToken());
        assertEquals("join", games.get(0).whiteUsername());
    }

    @Test
    void joinGameNegative() throws ClientException{
        facade.register("join1", "pass", "join1@g");
        var auth = facade.login("join1", "pass");
        assertThrows(ClientException.class, () -> facade.joinGame(auth.authToken(), 99999, "WHITE"));
    }


}
