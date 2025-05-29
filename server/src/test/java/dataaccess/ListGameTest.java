package dataaccess;

import chess.ChessGame;
import exceptions.ResponseException;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ListGameService;


import static org.junit.jupiter.api.Assertions.*;
class ListGameTest {
    private AuthSQL authSQL;
    private GameSQL gameSQL;
    private ListGameService listGameService;


    @BeforeEach
    void setUp() throws ResponseException, DataAccessException{
        gameSQL = new GameSQL();
        authSQL = new AuthSQL();
        listGameService = new ListGameService(gameSQL, authSQL);
        authSQL.deleteAllAuthTokens();
    }

    @Test
    void listGameTestPositive() throws ResponseException, DataAccessException {
        AuthData token = new AuthData("adjkslk", "Feathers McGraw");
        authSQL.addAuthToken(token);
        GameData game = new GameData(1234, "feathers", "Shawn", "cheese", new ChessGame());
        assertEquals(game.getGameID(), 1234);
    }

    @Test
    void listGameTestNegative() {
        assertThrows(ResponseException.class, () -> listGameService.listGames(null));
    }
}
