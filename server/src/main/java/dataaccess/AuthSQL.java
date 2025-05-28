package dataaccess;

import com.google.gson.Gson;
import exceptions.ResponseException;
import dataaccess.DataAccessException;
import model.AuthData;
import model.ModelType;

import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;


public class AuthSQL implements AuthDataAccess {


    public AuthSQL() throws ResponseException, DataAccessException {
        DataAccessHelper.configureDatabase(createStatements);
    }

    public AuthData addAuthToken(AuthData authToken) throws ResponseException, DataAccessException {
        var statement = "INSERT INTO authData (authToken, username, json) VALUES (?, ?, ?)";
        var json = new Gson().toJson(authToken);
        DataAccessHelper.executeUpdate(statement, authToken.getAuthToken(), authToken.getUsername(), json);
        return new AuthData(authToken.getAuthToken(), authToken.getUsername());
    }

    public AuthData getAuthToken(String authToken) throws ResponseException, DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, json FROM authData WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuthToken(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public void deleteAuthToken(String authToken) throws ResponseException, DataAccessException {
        var statement = "DELETE FROM authData WHERE authToken=?";
        DataAccessHelper.executeUpdate(statement, authToken);
    }

    public void deleteAllAuthTokens() throws ResponseException, DataAccessException {
        var statement = "DELETE FROM authData";
        DataAccessHelper.executeUpdate(statement);
    }

    private AuthData readAuthToken(ResultSet rs) throws SQLException {
        var authToken = rs.getString("authToken");
        var json = rs.getString("json");
        var token = new Gson().fromJson(json, AuthData.class);
        return token.setAuthToken(authToken);
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  authData (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`authToken`),
              INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
}
