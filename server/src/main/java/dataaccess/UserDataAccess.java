package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.Collection;


public interface UserDataAccess {
    void addUser(UserData username) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void deleteAllUsers() throws DataAccessException;

}
