package dataaccess;

import dataaccess.*;
import exceptions.ResponseException;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.JoinGameService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


class JoinGameTest {
    private AuthSQL authSQL;
    private GameSQL gameSQL;
    private JoinGameService joinGameService;


    @BeforeEach
    void setUp() throws ResponseException, DataAccessException {
        gameSQL = new GameSQL();
        authSQL = new AuthSQL();
        joinGameService = new JoinGameService(gameSQL, authSQL);
        authSQL.deleteAllAuthTokens();
        gameSQL.deleteAllGames();
    }

    @Test
    void joinGameTestPositive() throws ResponseException, DataAccessException {
        AuthData token = new AuthData("shjdkadj", "Feathers McGraw");
        authSQL.addAuthToken(token);
        int gameID = gameSQL.addGame("game", null, null);
        joinGameService.joinGame("shjdkadj", gameID, "WHITE");
        GameData joinGame = gameSQL.getGame(gameID);
        assertEquals("Feathers McGraw", joinGame.getWhiteUsername());
    }

    @Test
    void logoutTestNegative() {
        assertThrows(ResponseException.class, () -> joinGameService.joinGame(null, 0, null));
    }
}
