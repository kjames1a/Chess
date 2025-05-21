package dataaccess;

import model.AuthData;

import java.util.Collection;

public interface AuthDataAccess {
    AuthData addAuthToken(AuthData username) throws DataAccessException;

    AuthData getAuthToken(String username) throws DataAccessException;

    void deleteAuthToken(String username) throws DataAccessException;

    void deleteAllAuthTokens() throws DataAccessException;

}
