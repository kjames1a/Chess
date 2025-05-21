package server;

import chess.ChessGame;
import dataaccess.ErrorResponse;
import com.google.gson.Gson;
import dataaccess.*;
import model.GameData;
import service.JoinGameService;
import spark.*;
import spark.Request;


class JoinGameHandler {
    private final JoinGameService joinGameService;

    public JoinGameHandler(GameDataAccess gameDataAccess, AuthDataAccess authDataAccess){
        this.joinGameService = new JoinGameService(gameDataAccess, authDataAccess);
    }

    public Object handle(Request req, Response res) throws ResponseException {
        Gson gson = new Gson();
        try {
            GameData gameData = gson.fromJson(req.body(), GameData.class);
            String authToken = req.headers("authorization");
            int gameID = gameData.getGameID();
            if (authToken == null) {
                throw new ResponseException(401, "Error: unauthorized");
            }
            GameData game = joinGameService.joinGame(authToken, gameID, ChessGame.TeamColor);
            res.status(200);
            return gson.toJson(new CreateGameResponse(game.getGameID()));
        } catch (ResponseException ex) {
            res.status(ex.StatusCode());
            return gson.toJson(new ErrorResponse(ex.getMessage()));
        } catch (DataAccessException ex) {
            res.status(500);
            return gson.toJson(new ErrorResponse(ex.getMessage()));
        }
    }
    record CreateGameResponse(int gameID) {}
}

