package server;

import com.google.gson.Gson;
import dataaccess.*;
import exceptions.ErrorResponse;
import exceptions.ResponseException;
import model.GameData;
import service.CreateGameService;
import spark.Request;
import spark.Response;


class CreateGameHandler {
    private final CreateGameService createGameService;

    public CreateGameHandler(GameDataAccess gameDataAccess, AuthDataAccess authDataAccess){
        this.createGameService = new CreateGameService(gameDataAccess, authDataAccess);
    }

    public Object handle(Request req, Response res) {
        Gson gson = new Gson();
        try {
            GameData gameData = gson.fromJson(req.body(), GameData.class);
            String authToken = req.headers("Authorization");
            String gameName = gameData.getGameName();
            String whiteUsername = gameData.getWhiteUsername();
            String blackUsername = gameData.getBlackUsername();
            if (authToken == null) {
                throw new ResponseException(401, "Error: unauthorized");
            } else if (gameName == null) {
                throw new ResponseException(400, "Error: bad request");
            }
            GameData game = createGameService.createGame(gameName, authToken, whiteUsername, blackUsername);
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


