package service;

import dataaccess.AuthDataAccess;
import dataaccess.ResponseException;
import dataaccess.DataAccessException;
import model.AuthData;

public class LogoutService {
    private final AuthDataAccess authDataAccess;

    public LogoutService(AuthDataAccess authDataAccess) {
        this.authDataAccess = authDataAccess;
    }

    public AuthData logout(String authToken) throws ResponseException, DataAccessException {
        AuthData authData = authDataAccess.getAuthToken(authToken);
        if (authData == null) {
            throw new ResponseException(401, "Error: Unauthorized");
        }
        authDataAccess.deleteAuthToken(authToken);
        return authData;
    }
}


