package server;

import com.google.gson.Gson;
import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import exceptions.ResponseException;
import model.GameData;
import spark.Request;
import spark.Response;

public class WatchGameHandler {
    private final GameDataAccess gameDataAccess;
    private final AuthDataAccess authDataAccess;

    public WatchGameHandler(GameDataAccess gameDataAccess, AuthDataAccess authDataAccess) {
        this.authDataAccess = authDataAccess;
        this.gameDataAccess = gameDataAccess;
    }

    public Object handle(Request req, Response res) throws ResponseException, DataAccessException {
        String authToken = req.headers("Authorization");
        int gameID = Integer.parseInt(req.params("gameID"));
        GameData gameData = gameDataAccess.getGame(gameID);
        if (authDataAccess.getAuthToken(authToken) == null){
            throw new ResponseException(401, "Error: Unauthorized");
        } else if (gameID <= 0) {
            throw new ResponseException(44, "Error: bad request");
        } else if (gameData == null) {
            throw new ResponseException(400, "Error: bad request");
        }
        res.type("application/json");
        return new Gson().toJson(gameData);
    }
}
