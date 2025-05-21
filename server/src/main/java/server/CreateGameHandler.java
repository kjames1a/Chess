package server;

import com.google.gson.Gson;
import dataaccess.*;
import model.GameData;
import service.CreateGameService;
import spark.Request;
import spark.Response;


class CreateGameHandler {
    private final CreateGameService createGameService;

    public CreateGameHandler(GameDataAccess gameDataAccess, AuthDataAccess authDataAccess){
        this.createGameService = new CreateGameService(gameDataAccess, authDataAccess);
    }

    public Object handle(Request req, Response res) throws ResponseException {
        Gson gson = new Gson();
        try {
            GameData gameData = gson.fromJson(req.body(), GameData.class);
            String authToken = req.headers("authorization");
            String gameName = gameData.getGameName();
            if (authToken == null) {
                throw new ResponseException(401, "Error: unauthorized");
            } else if (gameName == null) {
                throw new ResponseException(400, "Error: bad request");
            }
            GameData game = createGameService.createGame(gameName, authToken);
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


