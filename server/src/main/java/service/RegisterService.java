package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.RegisterResult;
import model.UserData;
import java.util.UUID;

public class RegisterService {
    private final DataAccess dataAccess;

    public RegisterService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public RegisterResult register(UserData userData) throws DataAccessException {
        String username = userData.getUsername();
        if (dataAccess.getUser(username) != null){
            return new RegisterResult(null, null, false);
        } else {
            String authToken = generateToken();
            dataAccess.addUser(userData);
            AuthData authData = new AuthData(authToken, username);
            dataAccess.addAuthToken(authData);
            return new RegisterResult(username, authToken, true);
        }
    }
}
