package model;

import chess.ChessGame;

public class GameData {
    private int gameID;
    private String whiteUsername;
    private String blackUsername;
    private String gameName;
    private ChessGame game;

    public GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        this.gameID = gameID;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.game = game;
    }

    public int getGameID() {
        return gameID;
    }

    public GameData setGameName(String gameName) {
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }

    public GameData setID(int gameID) {
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public String getGameName() {
        return gameName;
    }

    public ChessGame getGame() {
        return game;
    }

    public void setWhiteUsername(String whiteUsername) { this.whiteUsername  = whiteUsername; }

    public void setBlackUsername(String blackUsername) { this.blackUsername = blackUsername; }

    private boolean gameEnd = false;

    public boolean isGameEnd() {
        return gameEnd;
    }

    public void setGameEnd(boolean gameEnd) {
        this.gameEnd=gameEnd;
    }
}
