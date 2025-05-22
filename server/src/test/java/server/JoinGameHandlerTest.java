package server;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import exceptions.ResponseException;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.JoinGameService;

import static org.junit.jupiter.api.Assertions.*;


class JoinGameHandlerTest {
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private JoinGameService joinGameService;


    @BeforeEach
    void setUp() {
        gameDAO = new GameDAO();
        authDAO = new AuthDAO();
        joinGameService = new JoinGameService(gameDAO, authDAO);
    }

    @Test
    void joinGameTestPositive() throws ResponseException, DataAccessException {
        AuthData token = new AuthData("ajkkjhjks", "Feathers McGraw");
        authDAO.addAuthToken(token);
        int gameID = gameDAO.addGame("game");
        joinGameService.joinGame("ajkkjhjks", gameID, "WHITE");
        assertNotNull(gameID);
    }

    @Test
    void logoutTestNegative() {
        assertThrows(ResponseException.class, () -> joinGameService.joinGame(null, 0, null));
    }
}