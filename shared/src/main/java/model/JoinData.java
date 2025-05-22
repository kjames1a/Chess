package model;

import chess.ChessGame;

public class JoinData {
    private int gameID;
    private String playerColor;

    public JoinData(int gameID, String playerColor) {
        this.gameID = gameID;
        this.playerColor = playerColor;
    }

    public int getGameID() {
        return gameID;
    }

    public String getTeamColor() { return playerColor; }
}
