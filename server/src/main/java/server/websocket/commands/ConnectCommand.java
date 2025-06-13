package server.websocket.commands;


import websocket.commands.UserGameCommand;

public class ConnectCommand extends UserGameCommand {
    public final String userColor;
    public final String userName;

    public ConnectCommand(String userName, String userColor) {
        this.userName = userName;
        this.userColor = userColor;
    }

    public String getUserColor() {
        return userColor;
    }
}
