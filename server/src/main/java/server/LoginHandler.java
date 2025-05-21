package server;

import com.google.gson.Gson;
import dataaccess.ErrorResponse;
import dataaccess.AuthDataAccess;
import dataaccess.ResponseException;
import dataaccess.UserDataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import service.LoginService;
import spark.Request;
import spark.Response;


class LoginHandler {
    private final LoginService loginService;

    public LoginHandler(UserDataAccess userDataAccess, AuthDataAccess authDataAccess){
        this.loginService = new LoginService(userDataAccess, authDataAccess);
    }

    public Object handle(Request req, Response res) throws ResponseException {
        Gson gson = new Gson();
        try {
            UserData userData = gson.fromJson(req.body(), UserData.class);
            if (userData.getUsername() == null) {
                throw new ResponseException(400, "Error: bad request");
            } else if (userData.getPassword() == null) {
                throw new ResponseException(400, "Error: bad request");
            }
            AuthData authData = loginService.login(userData);
            res.status(200);
            return gson.toJson(authData);
        } catch (ResponseException ex) {
            res.status(ex.StatusCode());
            return gson.toJson(new ErrorResponse(ex.getMessage()));
        } catch (DataAccessException ex) {
            res.status(400);
            return gson.toJson(new ErrorResponse(ex.getMessage()));
        }  catch (Exception ex) {
            res.status(500);
            return gson.toJson(new ErrorResponse(ex.getMessage()));
        }
    }
}
