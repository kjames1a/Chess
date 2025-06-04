package server;

import com.google.gson.Gson;
import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import exceptions.ResponseException;
import dataaccess.UserDataAccess;
import exceptions.ErrorResponse;
import model.AuthData;
import model.UserData;
import service.RegisterService;
import spark.*;
import spark.Request;


public class RegisterHandler {
    private final RegisterService registerService;

    public RegisterHandler(UserDataAccess userDataAccess, AuthDataAccess authDataAccess){
        this.registerService = new RegisterService(userDataAccess, authDataAccess);
    }

    public Object handle(Request req, Response res) throws ResponseException {
        Gson gson = new Gson();
        try {
            UserData userData = gson.fromJson(req.body(), UserData.class);
            if (userData.getUsername() == null) {
                throw new ResponseException(400, "Error: bad request");
            } else if (userData.getPassword() == null) {
                throw new ResponseException(400, "Error: bad request");
            } else if (userData.getEmail() == null) {
                throw new ResponseException(400, "Error: bad request");
            }
            AuthData authData = registerService.register(userData);
            res.status(200);
            return gson.toJson(authData);
        } catch (ResponseException ex) {
            res.status(ex.statusCode());
            return gson.toJson(new ErrorResponse(ex.statusCode(), ex.getMessage()));
        } catch (DataAccessException ex) {
            res.status(500);
            return gson.toJson(new ErrorResponse(500, ex.getMessage()));
        }
    }
    public record RegisterRequest(String username, String password, String email) {}
}
