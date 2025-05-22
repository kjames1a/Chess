package service;

import chess.ChessGame;
import dataaccess.*;
import exceptions.ResponseException;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
class ListGameHandlerTest {
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private ListGameService listGameService;


    @BeforeEach
    void setUp() {
        gameDAO = new GameDAO();
        authDAO = new AuthDAO();
        listGameService = new ListGameService(gameDAO, authDAO);
    }

    @Test
    void listGameTestPositive() throws ResponseException, DataAccessException {
        AuthData token = new AuthData("ajkkjhjks", "Feathers McGraw");
        authDAO.addAuthToken(token);
        GameData game = new GameData(1234, "feathers", "Shawn", "cheese", new ChessGame());
        assertEquals(game.getGameID(), 1234);
    }

    @Test
    void listGameTestNegative() {
        assertThrows(ResponseException.class, () -> listGameService.listGames(null));
    }
}