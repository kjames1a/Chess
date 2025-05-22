package service;

import dataaccess.AuthDataAccess;
import exceptions.ResponseException;
import dataaccess.DataAccessException;
import model.AuthData;

public class LogoutService {
    private final AuthDataAccess authDataAccess;

    public LogoutService(AuthDataAccess authDataAccess) {
        this.authDataAccess = authDataAccess;
    }

    public void logout(String authToken) throws ResponseException, DataAccessException {
        if (authToken == null) {
            throw new ResponseException(401, "Error: Unauthorized");
        }
        AuthData authData = authDataAccess.getAuthToken(authToken);
        if (authData == null) {
            throw new ResponseException(401, "Error: Unauthorized");
        }
        authDataAccess.deleteAuthToken(authToken);
    }
}


