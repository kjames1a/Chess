package dataaccess;

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

    public void addUser(UserData username) {
        users.add(username);
    }

    public void deleteAllUsers(){
        users.clear();
    }
}
