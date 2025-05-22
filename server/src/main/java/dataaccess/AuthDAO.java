package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Collection;

public class AuthDAO implements AuthDataAccess{
    private final Collection<AuthData> tokens = new ArrayList<>();

    public AuthData getAuthToken(String authToken) {
        for (AuthData token : tokens) {
            if (token.getAuthToken().equals(authToken)) {
                return token;
            }
        }
        return null;
    }

    public AuthData addAuthToken(AuthData authToken) {
        tokens.add(authToken);
        return authToken;
    }

    public void deleteAuthToken(String authToken) {
        for (AuthData token : tokens){
            if (token.getAuthToken().equals(authToken)){
                tokens.remove(token);
                break;
            }
        }
    }


    public void deleteAllAuthTokens(){
        tokens.clear();
    }
}

