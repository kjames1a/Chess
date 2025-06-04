package server;

import com.google.gson.Gson;
import dataaccess.*;
import exceptions.ErrorResponse;
import exceptions.ResponseException;
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
            String authToken = req.headers("Authorization");
            if (authToken == null) {
                return gson.toJson(new ErrorResponse(401, "Error: Unauthorized"));
            }
            Collection<GameData> gameData = listGameService.listGames(authToken);
            res.status(200);
            return gson.toJson(new GameListResponse(gameData));
        } catch (ResponseException ex) {
            res.status(ex.statusCode());
            return gson.toJson(new ErrorResponse(ex.statusCode(), ex.getMessage()));
        } catch (DataAccessException ex) {
            res.status(500);
            return gson.toJson(new ErrorResponse(500, ex.getMessage()));
        }
    }
    public record GameListResponse(Collection<GameData> games) {}
}
