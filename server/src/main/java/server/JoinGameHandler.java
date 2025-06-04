package server;

import exceptions.ErrorResponse;
import com.google.gson.Gson;
import dataaccess.*;
import exceptions.ResponseException;
import model.GameData;
import model.JoinData;
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
            JoinData joinData = gson.fromJson(req.body(), JoinData.class);
            String authToken = req.headers("Authorization");
            int gameID = joinData.getGameID();
            String playerColor = joinData.getPlayerColor();
            GameData game = joinGameService.joinGame(authToken, gameID, playerColor);
            res.status(200);
            return gson.toJson(new CreateGameResponse(game.getGameID()));
        } catch (ResponseException ex) {
            res.status(ex.statusCode());
            return gson.toJson(new ErrorResponse(ex.statusCode(), ex.getMessage()));
        } catch (DataAccessException ex) {
            res.status(500);
            return gson.toJson(new ErrorResponse(500, ex.getMessage()));
        }
    }
    record CreateGameResponse(int gameID) {}

}

