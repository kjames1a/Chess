package server;

import com.google.gson.Gson;
import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import exceptions.ErrorResponse;
import exceptions.ResponseException;
import model.GameData;
import model.JoinData;
import service.JoinGameService;
import spark.Request;
import spark.Response;

public class WatchGameHandler {
    private final JoinGameService joinGameService;

    public WatchGameHandler(GameDataAccess gameDataAccess, AuthDataAccess authDataAccess) {
        this.joinGameService = new JoinGameService(gameDataAccess, authDataAccess);
    }

    public Object handle(Request req, Response res) throws ResponseException, DataAccessException {
        Gson gson = new Gson();
        try {
            JoinData joinData = gson.fromJson(req.body(), JoinData.class);
            int gameID = joinData.getGameID();
            String authToken = req.headers("Authorization");
            if (authToken == null) {
                throw new ResponseException(401, "Error: Unauthorized");
            } else if (joinData == null) {
                throw new ResponseException(400, "Error: bad request");
            } else if (gameID <= 0) {
                throw new ResponseException(400, "Error: bad request");
            }
            GameData watch = joinGameService.joinGame(authToken, gameID, null);
            res.status(200);
            return gson.toJson(new WatchGameResponse(watch.getGameID()));
        } catch (ResponseException ex) {
            res.status(ex.statusCode());
            return gson.toJson(new ErrorResponse(ex.statusCode(), ex.getMessage()));
        } catch (DataAccessException ex) {
            res.status(500);
            return gson.toJson(new ErrorResponse(500, ex.getMessage()));
        }
    }
    record WatchGameResponse(int gameID) {}
}
