package server;

import com.google.gson.Gson;
import dataaccess.AuthDataAccess;
import exceptions.ResponseException;
import exceptions.ErrorResponse;
import service.LogoutService;
import spark.Request;
import spark.Response;


class LogoutHandler {
    private final LogoutService logoutService;

    public LogoutHandler(AuthDataAccess authDataAccess) {
        this.logoutService = new LogoutService(authDataAccess);
    }

    public Object handle(Request req, Response res) throws ResponseException {
        Gson gson = new Gson();
        try {
            String authToken = req.headers("authorization");
            logoutService.logout(authToken);
            res.status(200);
            return "{}";
        } catch (ResponseException ex) {
            res.status(ex.statusCode());
            return gson.toJson(new ErrorResponse(ex.statusCode(), ex.getMessage()));
        } catch (Exception ex) {
            res.status(500);
            return gson.toJson(new ErrorResponse(500, ex.getMessage()));
        }
    }
}
