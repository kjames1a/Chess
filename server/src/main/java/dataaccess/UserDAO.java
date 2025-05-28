package dataaccess;

import exceptions.ResponseException;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;

public class UserDAO implements UserDataAccess {
    private final Collection<UserData> users = new ArrayList<>();

    public UserData getUser(String username) {
        for (UserData user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public UserData setPassword(String username, String hashedPassword) {
        for (UserData user : users) {
            if (user.getUsername().equals(username)) {
                user.setPassword(hashedPassword);
                return user;
            }
        }
        return null;
    }

    public String readHashedPasswordFromDatabase(String username) {
        for (UserData user : users) {
            if (user.getUsername().equals(username)) {
                return user.getPassword();
            }
        }
        return null;
    }

    public UserData addUser(UserData username) {
        users.add(username);
        return username;
    }

    public void deleteAllUsers(){
        users.clear();
    }
}
