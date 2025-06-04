package dataaccess;

import exceptions.ResponseException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.CreateGameService;
import service.LoginService;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ClearAllTest {
    private AuthSQL authSQL;
    private GameSQL gameSQL;
    private UserSQL userSQL;;


    @BeforeEach
    void setup() throws ResponseException, DataAccessException{
        this.authSQL = new AuthSQL();
        this.gameSQL = new GameSQL();
        this.userSQL = new UserSQL();
        authSQL.deleteAllAuthTokens();
        gameSQL.deleteAllGames();
        userSQL.deleteAllUsers();
    }

    @Test
    void clearAllTestPositive() throws ResponseException, DataAccessException {
        authSQL.addAuthToken(new AuthData("ghsjkh", "Gromit"));
        int gameID = gameSQL.addGame("game", "wallace", "cheese");
        userSQL.addUser(new UserData("Gromit", "wowow", "gromit1@gmail.com"));
        authSQL.deleteAllAuthTokens();
        gameSQL.deleteAllGames();
        userSQL.deleteAllUsers();
        assertNull(authSQL.getAuthToken("ghsjkh"));
        assertNull(userSQL.getUser("Gromit"));
        assertNull(gameSQL.getGame(gameID));
    }



}

