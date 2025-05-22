package server;

import chess.ChessGame;
import dataaccess.*;
import exceptions.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.CreateGameService;


import static org.junit.jupiter.api.Assertions.*;
class CreateGameHandlerTest {
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private CreateGameService createGameService;


    @BeforeEach
    void setUp() {
        gameDAO = new GameDAO();
        authDAO = new AuthDAO();
        createGameService = new CreateGameService(gameDAO, authDAO);
    }

    @Test
    void createGameTestPositive() throws ResponseException, DataAccessException {
        AuthData authData = new AuthData("jkhjkhj", "Feathers");
        authDAO.addAuthToken(authData);
        GameData game = createGameService.createGame("game", authData.getAuthToken());
        assertEquals("game", game.getGameName());
    }

    @Test
    void createGameTestNegative() {
        assertThrows(ResponseException.class, () -> createGameService.createGame("game", null));
    }
}