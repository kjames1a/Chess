package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exceptions.ResponseException;
import dataaccess.DataAccessException;
import model.GameData;
import model.ModelType;

import java.util.ArrayList;
import java.util.Collection;
import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;


public class GameSQL implements GameDataAccess {


    public GameSQL() throws ResponseException, DataAccessException {
        configureDatabase();
    }


    public int addGame(String gameName, String whiteUsername, String blackUsername) throws ResponseException, DataAccessException {
        var statement = "INSERT INTO gameData (whiteUsername, blackUsername, gameName, game, json) VALUES (?, ?, ?, ?, ?)";
        ChessGame chessGame = new ChessGame();
        GameData gameData = new GameData(0, whiteUsername, blackUsername, gameName, chessGame);
        var gameJSON = new Gson().toJson(chessGame);
        var json = new Gson().toJson(gameData);
        return ExecuteUpdate.executeUpdate(statement, whiteUsername, blackUsername, gameName, gameJSON, json);
    }

    public GameData getGame(int id) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, whiteUsername, blackUsername, gameName, json FROM gameData WHERE id=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, id);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(500, String.format("Error: Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public GameData update(GameData game) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "UPDATE gameData SET whiteUsername = ?, blackUsername = ? WHERE id = ?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, game.getWhiteUsername());
                ps.setString(2, game.getBlackUsername());
                ps.setInt(3, game.getGameID());
                try {
                    int gameNum = ps.executeUpdate();
                    if (gameNum == 0) {
                        throw new DataAccessException("Error: No games found");
                    }
                    return game;
                } catch (Exception e) {
                    throw new DataAccessException("Error: Game update fail");
                }
            } catch (Exception e) {
                throw new DataAccessException("Error: Failed to retrieve game data");
            }
        } catch (Exception e) {
            throw new DataAccessException("Error: Failed to connect to database");
        }
    }

    public Collection<GameData> listGame() throws ResponseException {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, whiteUsername, blackUsername, json FROM gameData";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readGame(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    public void deleteAllGames() throws ResponseException, DataAccessException {
        var statement = "DELETE FROM gameData";
        ExecuteUpdate.executeUpdate(statement);
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        var id = rs.getInt("id");
        var json = rs.getString("json");
        var game = new Gson().fromJson(json, GameData.class);
        game.setWhiteUsername(rs.getString("whiteUsername"));
        game.setBlackUsername(rs.getString("blackUsername"));
        return game.setID(id);
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS gameData (
              `id` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256) NOT NULL,
              `blackUsername` varchar(256) NOT NULL,
              `gameName` varchar(256) NOT NUll,
              `game` TEXT DEFAULT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`)
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
