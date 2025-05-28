package dataaccess;

import exceptions.ResponseException;
import model.AuthData;

import java.util.Collection;

public interface AuthDataAccess {
    AuthData addAuthToken(AuthData authData) throws DataAccessException, ResponseException;

    AuthData getAuthToken(String authToken) throws DataAccessException, ResponseException;

    void deleteAuthToken(String authToken) throws DataAccessException, ResponseException;

    void deleteAllAuthTokens() throws DataAccessException, ResponseException;

}
