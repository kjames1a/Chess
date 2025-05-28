package service;

import dataaccess.AuthDataAccess;
import exceptions.ResponseException;
import dataaccess.UserDataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

    public class LoginService {
        private final UserDataAccess userDataAccess;
        private final AuthDataAccess authDataAccess;

        public LoginService(UserDataAccess userDataAccess, AuthDataAccess authDataAccess) {
            this.userDataAccess = userDataAccess;
            this.authDataAccess = authDataAccess;
        }

        public static String generateToken() {
            return UUID.randomUUID().toString();
        }

        public AuthData login(UserData userData) throws ResponseException, DataAccessException {
            if (userData == null) {
                throw new ResponseException(401, "Error: Unauthorized");
            }
            String username = userData.getUsername();
            String password = userData.getPassword();
            UserData user = userDataAccess.getUser(username);
            if (!verifyUser(username, password)) {
                throw new ResponseException(401, "Error: Unauthorized");
            } else if (user == null) {
                throw new ResponseException(401, "Error: Unauthorized");
            } else if (user.getUsername() == null) {
                throw new ResponseException(401, "Error: Unauthorized");
            } else if (user.getPassword() == null) {
                throw new ResponseException(401, "Error: Unauthorized");
            } else if (!user.getUsername().equals(username)) {
                throw new ResponseException(401, "Error: Unauthorized");
            }
            String authToken = generateToken();
            AuthData authData = new AuthData(authToken, username);
            authDataAccess.addAuthToken(authData);
            verifyUser(username, password);
            return authData;
        }

        boolean verifyUser(String username, String providedClearTextPassword) throws ResponseException, DataAccessException{
        var hashedPassword = userDataAccess.readHashedPasswordFromDatabase(username);
        if (hashedPassword == null) {
            throw new ResponseException(401, "Error: Unauthorized");
        }
        return BCrypt.checkpw(providedClearTextPassword, hashedPassword);
    }
    }

