package dataaccess;

import chess.ChessGame;
import exceptions.ResponseException;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class GameDAO implements GameDataAccess {
    int nextGameID = 1;
    final private HashMap<Integer, GameData> gameData = new HashMap<>();


    public GameData getGame(int gameID) {
        return gameData.get(gameID);
    }

    public GameData update(GameData game) throws ResponseException{
        if(gameData.get(game.getGameID()) == null) {
            throw new ResponseException(400, "Error: Game not found");
        }
        gameData.put(game.getGameID(), game);
        return game;
    }

    public int addGame(String gameName, String whiteUsername, String blackUsername) {
        int gameID = nextGameID++;
        GameData newGame = new GameData(gameID, null, null, gameName, new ChessGame());
        this.gameData.put(gameID, newGame);
        return gameID;
    }

    public Collection<GameData> listGame() {
        return gameData.values();
    }

    public void deleteAllGames(){
        gameData.clear();
    }
}
