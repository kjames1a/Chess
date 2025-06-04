package client;

import chess.ChessGame;
import exceptions.ResponseException;
import model.AuthData;
import model.GameData;
import model.JoinData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        String portNum = String.valueOf(port);
        System.out.println("Started test HTTP server on " + portNum);
        String url = "http://localhost:" + portNum;
        facade = new ServerFacade(url);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    void register() throws Exception {
        UserData user = new UserData("feathers", "chicken", "gmail.com");
        var authData = facade.createUser(user);
        assertTrue(authData.getAuthToken().length() > 10);
    }

    @Test
    void registerTestNeg() {
        assertThrows(ResponseException.class, () -> facade.createUser(null));
    }

    @Test
    void login() throws Exception {
        UserData user = new UserData("feathers", "chicken", null);
        var authData = facade.loginUser(user);
        assertTrue(authData.getUsername().equals(user.getUsername()));
    }

    @Test
    void loginTestNeg() {
        assertThrows(ResponseException.class, () -> facade.loginUser(null));
    }
    @Test
    void listGames() throws Exception {
        var authData = new AuthData("hjksjdhfhjdhsjkfhjdk", "feathers");
        var gameData = facade.listGames(authData.getAuthToken());
        assertTrue(gameData.size() > 10);
    }

    @Test
    void listGamesNeg() {
        assertThrows(ResponseException.class, () -> facade.listGames(null));
    }

    @Test
    void createGame() throws Exception {
        UserData user = new UserData("player3", "11111", "em.email");
        facade.createUser(user);
        var auth = facade.loginUser(user);
        var authToken = auth.getAuthToken();
        GameData gameData = new GameData(0, null, null, "gameName", new ChessGame());
        var createGame = facade.createGame(gameData, authToken);
        System.out.println("Created Game" + createGame.getGameName());
        assertTrue(createGame.getGameName().equals(gameData.getGameName()));
    }

    @Test
    void createGameNeg() {
        assertThrows(ResponseException.class, () -> facade.createGame(null, "ghsjkdhjskdhkjshdk"));
    }

    @Test
    void joinGame() throws ResponseException {
        UserData user = new UserData("player9", "11111", "em.aehgail");
        facade.createUser(user);
        var auth = facade.loginUser(user);
        var authToken = auth.getAuthToken();
        System.out.println(authToken);
        JoinData game = new JoinData(4, "WHITE", "cheese");
        AuthData authData = new AuthData(authToken, "player9");
        var join = facade.joinGame(game, authData.getAuthToken());
        assertTrue(join.getGameName().equals(game.getGameName()));
    }

    @Test
    void joinGameNeg() {
        assertThrows(ResponseException.class, () -> facade.joinGame(null, "hsjkjshjskdjfhdjkjsdh"));
    }

    @Test
    void watchGame() throws Exception {
        JoinData game = new JoinData(4, "WHITE", "cheese");
        AuthData authData = new AuthData("ghjskjshjdkjhjdk", "feathers");
        var auth = facade.watchGame(game, authData.getAuthToken());
        assertTrue(auth.getGameID() == (game.getGameID()));
    }

    @Test
    void watchGameNeg() {
        assertThrows(ResponseException.class, () -> facade.watchGame(null, "shjdksjhdkjdhkjsdhksj"));
    }

    @Test
    void logout() throws Exception {
        UserData user = new UserData("player13", "11111", "em.aehgail");
        facade.createUser(user);
        var auth = facade.loginUser(user);
        var authToken = auth.getAuthToken();
        facade.logoutUser(authToken);
        assertFalse(authToken == null);
    }

    @Test
    void logoutNeg() {
        assertThrows(ResponseException.class, () -> facade.logoutUser(null));
    }




}
