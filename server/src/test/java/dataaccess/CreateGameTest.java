package dataaccess;

import dataaccess.*;
import exceptions.ResponseException;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.CreateGameService;


import static org.junit.jupiter.api.Assertions.*;
class CreateGameTest {
    private AuthSQL authSQL;
    private GameSQL gameSQL;
    private CreateGameService createGameService;


    @BeforeEach
    void setUp() throws ResponseException, DataAccessException{
        gameSQL = new GameSQL();
        authSQL = new AuthSQL();
        createGameService = new CreateGameService(gameSQL, authSQL);
        authSQL.deleteAllAuthTokens();
    }

    @Test
    void createGameTestPositive() throws ResponseException, DataAccessException {
        AuthData authData = new AuthData("jkhjkhj", "Feathers");
        authSQL.addAuthToken(authData);
        GameData game = createGameService.createGame("game", authData.getAuthToken(), "feathers", "cheese");
        assertEquals("game", game.getGameName());
    }

    @Test
    void createGameTestNegative() {
        assertThrows(ResponseException.class, () -> createGameService.createGame("game", null, "Feathers", "Cheese"));
    }
}
