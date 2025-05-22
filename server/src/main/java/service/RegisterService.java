package service;

import dataaccess.AuthDataAccess;
import exceptions.ResponseException;
import dataaccess.UserDataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import java.util.UUID;

public class RegisterService {
    private final UserDataAccess userDataAccess;
    private final AuthDataAccess authDataAccess;

    public RegisterService(UserDataAccess userDataAccess, AuthDataAccess authDataAccess) {
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public AuthData register(UserData userData) throws ResponseException, DataAccessException {
        if (userData == null) {
            throw new ResponseException(400, "Error: bad request");
        }
        String username = userData.getUsername();
        String password = userData.getPassword();
        String email = userData.getEmail();
        UserData user = userDataAccess.getUser(username);
        if (user != null) {
            throw new ResponseException(403, "Error: already taken");
        } else if (username == null || password == null || email == null) {
            throw new ResponseException(400, "Error: bad request");
        }
        String authToken = generateToken();
        userDataAccess.addUser(userData);
        AuthData authData = new AuthData(authToken, username);
        authDataAccess.addAuthToken(authData);
        return authData;
    }
}


