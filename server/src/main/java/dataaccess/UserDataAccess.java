package dataaccess;

import exceptions.ResponseException;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;

import java.util.Collection;


public interface UserDataAccess {
    UserData addUser(UserData username) throws DataAccessException, ResponseException;

    UserData getUser(String username) throws DataAccessException, ResponseException;

    void deleteAllUsers() throws DataAccessException, ResponseException;

    UserData setPassword(String password, String username) throws DataAccessException, ResponseException;

    String readHashedPasswordFromDatabase(String username) throws DataAccessException, ResponseException;
}
