package model;

public class JoinData {
    private final int gameID;
    private final String playerColor;
    private final String gameName;

    public JoinData(int gameID, String playerColor, String gameName) {
        this.gameID = gameID;
        this.playerColor = playerColor;
        this.gameName = gameName;
    }

    public int getGameID() {
        return gameID;
    }

    public String getPlayerColor() { return playerColor; }

    public String getGameName() { return gameName; };
}
