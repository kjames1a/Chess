package service;

import chess.ChessGame;
import dataaccess.*;
import model.*;


public class JoinGameService {
    private final GameDataAccess gameDataAccess;
    private final AuthDataAccess authDataAccess;

    public JoinGameService(GameDataAccess gameDataAccess, AuthDataAccess authDataAccess) {
        this.gameDataAccess = gameDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public GameData joinGame(String authToken, int gameID, ChessGame.TeamColor color) throws ResponseException, DataAccessException {
        AuthData authData = authDataAccess.getAuthToken(authToken);
        GameData gameData = gameDataAccess.getGame(gameID);
        if (authData == null) {
            throw new ResponseException(401, "Error: Unauthorized");
        } else if (gameData == null) {
            throw new ResponseException(400, "Error: bad request");
        }
        String username = authData.getUsername();
        if (color == ChessGame.TeamColor.BLACK) {
            if (gameData.getBlackUsername() != null) {
                throw new ResponseException(403, "Error: already taken");
            }
            gameData.setBlackUsername(username);
        } else if (color == ChessGame.TeamColor.WHITE) {
            if (gameData.getBlackUsername() != null) {
                throw new ResponseException(403, "Error: already taken");
            }
            gameData.setWhiteUsername(username);
        }
        gameDataAccess.joinGame(gameData);
        return gameData;
    }
}




