package dataaccess;


import com.google.gson.Gson;
import exceptions.ResponseException;
import model.ModelType;
import model.UserData;

import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;


public class UserSQL implements UserDataAccess {

    public UserSQL() throws ResponseException, DataAccessException {
        configureDatabase();
    }

    public UserData setPassword(String username, String hashedPassword) throws DataAccessException, ResponseException {
        var statement = "UPDATE userData SET password = ? WHERE username = ?";
        ExecuteUpdate.executeUpdate(statement, hashedPassword, username);
        return getUser(username);
    }

    public String readHashedPasswordFromDatabase(String username) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT password, json FROM userData WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("password");
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(500, String.format("Error: Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public UserData addUser(UserData userData) throws ResponseException, DataAccessException {
        var statement = "INSERT INTO userData (username, password, email, json) VALUES (?, ?, ?, ?)";
        var json = new Gson().toJson(userData);
        ExecuteUpdate.executeUpdate(statement, userData.getUsername(), userData.getPassword(), userData.getEmail(), json);
        return new UserData(userData.getUsername(), userData.getPassword(), userData.getEmail());
    }

    public UserData getUser(String username) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, json FROM userData WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUsername(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(500, String.format("Error: Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public void deleteAllUsers() throws ResponseException, DataAccessException {
        var statement = "DELETE FROM userData";
        ExecuteUpdate.executeUpdate(statement);
    }

    private UserData readUsername(ResultSet rs) throws SQLException {
        var id = rs.getString("username");
        var json = rs.getString("json");
        var user = new Gson().fromJson(json, UserData.class);
        return user.setUsername(id);
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS userData (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`username`),
              INDEX(password),
              INDEX(email)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws ResponseException, DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new ResponseException(500, String.format("Error: Unable to configure database: %s", ex.getMessage()));
        }
    }
}
