package model;

public class RegisterResult {
    private final String username;
    private final String authToken;
    private final boolean success;

    public RegisterResult(String username, String authToken, boolean success){
        this.username = username;
        this.authToken = authToken;
        this.success = success;
    }

    public String getUsername(){
        return username;
    }

    public String getAuthToken(){
        return authToken;
    }

    public boolean isSuccess() {
        return success;
    }
}
