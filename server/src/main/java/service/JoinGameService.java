package service;

import chess.ChessGame;
import dataaccess.*;
import exceptions.ResponseException;
import model.*;


public class JoinGameService {
    private final GameDataAccess gameDataAccess;
    private final AuthDataAccess authDataAccess;

    public JoinGameService(GameDataAccess gameDataAccess, AuthDataAccess authDataAccess) {
        this.gameDataAccess = gameDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public GameData joinGame(String authToken, int gameID, String color) throws ResponseException, DataAccessException {
        AuthData authData = authDataAccess.getAuthToken(authToken);
        GameData gameData = gameDataAccess.getGame(gameID);
        if (authData == null) {
            throw new ResponseException(401, "Error: Unauthorized");
        } else if (gameData == null) {
            throw new ResponseException(400, "Error: bad request");
        }
        String username = authData.getUsername();
        if (color == null) {
            throw new ResponseException(400, "Error: bad request");
        } else if (color.equals("BLACK")) {
            if (gameData.getBlackUsername() != null) {
                throw new ResponseException(403, "Error: already taken");
            }
            gameData.setBlackUsername(username);
            gameDataAccess.update(gameData);
        } else if (color.equals("WHITE")) {
            if (gameData.getWhiteUsername() != null) {
                throw new ResponseException(403, "Error: already taken");
            }
            gameData.setWhiteUsername(username);
            gameDataAccess.update(gameData);
        } else if (!color.equals("BLACK") && !color.equals("WHITE")) {
        throw new ResponseException(400, "Error: bad request");
        }
        return gameData;
    }
}




