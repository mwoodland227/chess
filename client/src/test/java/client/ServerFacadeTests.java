package client;

import dataclasses.AuthData;
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
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

//    @BeforeEach
//    public void setUp(){
//        facade = new ServerFacade("http://localhost:8081");
//    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


//    @BeforeEach
//    void clearData(){
//        try{
//            var auth = facade.register("test", "test", "test");
//            if(auth != null) {
//                facade.logout(auth.authToken());
//            }
//        } catch(Exception ignored){}
//    }

    @Test
    void registerPositive() throws ClientException{
        UserData user = facade.register("cams", "pass", "c@g");
        assertNotNull(user);
        assertEquals("cams", user.username());
    }

    @Test
    void registerNegative() throws ClientException{
        facade.register("cams", "pass", "c@g");
        assertThrows(ClientException.class, () ->
                facade.register("cams", "pass1", "c2@g"));
    }


//    @Test
//    void loginPositive() throws ClientException{
//        facade.register("cams", "pass", "c@g");
//        AuthData auth = facade.login("cams", "pass");
//
//        assertNotNull(au);
//    }

}
