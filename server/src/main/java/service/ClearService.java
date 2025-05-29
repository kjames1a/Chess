package service;

import dataaccess.AuthSQL;
import dataaccess.DataAccessException;
import dataaccess.GameSQL;
import dataaccess.UserSQL;
import exceptions.ResponseException;

public class ClearService {
    private final AuthSQL authSQL;
    private final UserSQL userSQL;
    private final GameSQL gameSQL;

    public ClearService(AuthSQL authSQL, UserSQL userSQL, GameSQL gameSQL) {
        this.authSQL = authSQL;
        this.userSQL = userSQL;
        this.gameSQL = gameSQL;
    }

    public void clear() throws ResponseException {
        if (authSQL == null || userSQL == null || gameSQL == null) {
            throw new ResponseException(500, "Error: Database issue");
        }
        try {
            authSQL.deleteAllAuthTokens();
            userSQL.deleteAllUsers();
            gameSQL.deleteAllGames();
        } catch (DataAccessException e) {
            throw new ResponseException(500, "Error: Database issue");
        }
    }
}
