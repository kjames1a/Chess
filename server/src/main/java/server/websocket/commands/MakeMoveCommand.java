package server.websocket.commands;

import chess.ChessMove;
import websocket.commands.UserGameCommand;

public class MakeMoveCommand extends UserGameCommand {
    public String username;
    public ChessMove move;

    public MakeMoveCommand(String username, ChessMove move) {
        this.username = username;
        this.move = move;
    }

    public ChessMove getMoveDescription() {
        return this.move;
    }

}
