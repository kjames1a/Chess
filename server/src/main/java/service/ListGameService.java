package service;

import dataaccess.*;
import exceptions.ResponseException;
import model.*;

import java.util.Collection;

public class ListGameService {
    private final GameDataAccess gameDataAccess;
    private final AuthDataAccess authDataAccess;

    public ListGameService(GameDataAccess gameDataAccess, AuthDataAccess authDataAccess) {
        this.gameDataAccess = gameDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public Collection<GameData> listGames(String authToken) throws ResponseException, DataAccessException {
        AuthData authData = authDataAccess.getAuthToken(authToken);
        if (authData == null) {
            throw new ResponseException(401, "Error: Unauthorized");
        } else if (!authToken.equals(authData.getAuthToken())) {
            throw new ResponseException(401, "Error: Unauthorized");
        } else {
            return gameDataAccess.listGame();
        }
    }
}


