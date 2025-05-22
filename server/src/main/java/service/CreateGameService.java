package service;

import dataaccess.*;
import exceptions.ResponseException;
import model.*;


public class CreateGameService {
    private final GameDataAccess gameDataAccess;
    private final AuthDataAccess authDataAccess;

    public CreateGameService(GameDataAccess gameDataAccess, AuthDataAccess authDataAccess) {
        this.gameDataAccess = gameDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public GameData createGame(String gameName, String authToken) throws ResponseException, DataAccessException {
        AuthData authData = authDataAccess.getAuthToken(authToken);
        if (authData == null) {
            throw new ResponseException(401, "Error: Unauthorized");
        }
        int gameID = gameDataAccess.addGame(gameName);
        GameData gameData = gameDataAccess.getGame(gameID);
        return gameData;
    }
}



