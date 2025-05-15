package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMoves {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> chessPieceMoves = new ArrayList<>();
        ChessPieceHelper queenMoves = new ChessPieceHelper();
        queenMoves.moveHelper(myPosition, chessPieceMoves, 1, 1, board);
        queenMoves.moveHelper(myPosition, chessPieceMoves, -1, -1, board);
        queenMoves.moveHelper(myPosition, chessPieceMoves, 1, -1, board);
        queenMoves.moveHelper(myPosition, chessPieceMoves, -1, 1, board);
        queenMoves.moveHelper(myPosition, chessPieceMoves, 1, 0, board);
        queenMoves.moveHelper(myPosition, chessPieceMoves, -1, 0, board);
        queenMoves.moveHelper(myPosition, chessPieceMoves, 0, 1, board);
        queenMoves.moveHelper(myPosition, chessPieceMoves, 0, -1, board);
        return chessPieceMoves;
    }
}
