package server;

import com.google.gson.Gson;
import dataaccess.*;
import model.GameData;
import service.ListGameService;
import spark.Request;
import spark.Response;

import java.util.Collection;


class ListGameHandler {
    private final ListGameService listGameService;

    public ListGameHandler(GameDataAccess gameDataAccess, AuthDataAccess authDataAccess){
        this.listGameService = new ListGameService(gameDataAccess, authDataAccess);
    }

    public Object handle(Request req, Response res) throws ResponseException {
        Gson gson = new Gson();
        try {
            String authToken = req.headers("authorization");
            Collection<GameData> gameData = listGameService.listGames(authToken);
            res.status(200);
            return gson.toJson(new GameListResponse(gameData));
        } catch (ResponseException ex) {
            res.status(ex.StatusCode());
            return gson.toJson(new ErrorResponse(ex.getMessage()));
        } catch (DataAccessException ex) {
            res.status(500);
            return gson.toJson(new ErrorResponse(ex.getMessage()));
        }
    }
    public record GameListResponse(Collection<GameData> games) {}
}
