package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import exceptions.ResponseException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;


public class ClearAllTest {
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private UserDAO userDAO;

    @BeforeEach
    void setup() {
        this.authDAO = new AuthDAO();
        this.gameDAO = new GameDAO();
        this.userDAO = new UserDAO();
    }

    @Test
    void clearAllTestPositive() throws ResponseException, DataAccessException {
        authDAO.addAuthToken(new AuthData("ghsjkh", "Feathers"));
        int gameID = gameDAO.addGame("game");
        userDAO.addUser(new UserData("Feathers", "wowow", "chicken@gmail.com"));
        authDAO.deleteAllAuthTokens();
        gameDAO.deleteAllGames();
        userDAO.deleteAllUsers();
        assertNull(authDAO.getAuthToken("ghsjkh"));
        assertNull(userDAO.getUser("Feathers"));
        assertNull(gameDAO.getGame(gameID));
    }


}
