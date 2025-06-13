package server.websocket.commands;

import websocket.commands.UserGameCommand;

public class ResignCommand extends UserGameCommand {
    public String userName;

   private boolean playerResign = false;


    public boolean setPlayerResign(boolean resigned) {
        return this.playerResign = resigned;
    }
}
