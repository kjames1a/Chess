package dataaccess;

import model.AuthData;

import java.util.Collection;

public interface AuthDataAccess {
    AuthData addAuthToken(AuthData authData) throws DataAccessException;

    AuthData getAuthToken(String authToken) throws DataAccessException;

    void deleteAuthToken(String authToken) throws DataAccessException;

    void deleteAllAuthTokens() throws DataAccessException;

}
