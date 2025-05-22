package passoff.server;

import chess.ChessGame;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChessServerTest {
    static ServerFacade server;
    static private int gameID;
    static private String gameName;
    static private String authToken;

    @BeforeAll
    static void setUp() {
        server = new ServerFacade("http://localhost:8080/");
        gameID = 1234;
        gameName = "game!";
        authToken = "token";
    }

    @AfterAll
    static void stopServer() {
        Server.stop();
    }

    @BeforeEach
    void clear() {
        assertDoesNotThrow(() -> server.deleteAllData());
    }

    @Test
    void register() throws Exception {
        var user = new UserData("Feathers McGraw", "cheese", "chicken@gmail.com");
        var result = assertDoesNotThrow(() -> server.createUser(user));
        assertEquals("Feathers McGraw", result.getUsername());
    }

    @Test
    void login() throws Exception {
        var user= new UserData("Feathers McGraw", "cheese", "chicken@gmail.com");
        server.createUser(user);
        var result = assertDoesNotThrow(() -> server.loginUser(user));
        assertEquals("Feathers McGraw", result.getUsername());
    }

    @Test
    void logout() throws Exception {
        var user= new UserData("Feathers", "Cheese", "chicken@gmail.com");
        var result = server.createUser(user);
        assertDoesNotThrow(() -> server.logoutUser(result.getAuthToken()));
        server.logoutUser(authToken);
    }

    @Test
    void listGames() throws Exception {
        var expected = new ArrayList<GameData>();
        expected.add(server.createGame(new GameData(gameID, "Feather", "Shawn", gameName, new ChessGame())));
        expected.add(server.createGame(new GameData(gameID, "Chicken", "Cheese", gameName, new ChessGame())));

        var result = assertDoesNotThrow(() -> server.listGames());
        assertEquals(expected, List.of(result));
    }

    @Test
    void createGame() throws Exception {
        var game = new GameData(gameID, "Feathers McGraw", "Shawn", gameName, new ChessGame());
        var result = assertDoesNotThrow(() -> server.createGame(game));
        assertEquals(game.getGameID(), result.getGameID());
    }

    @Test
    void joinGame() throws Exception {
        var game = new GameData(gameID, "Feathers McGraw", "Shawn", gameName, new ChessGame());
        assertDoesNotThrow(() -> server.joinGame(game));
    }

}
