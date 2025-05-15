package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMoves {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> chessPieceMoves = new ArrayList<>();
        ChessPieceHelper bishopMoves = new ChessPieceHelper();
        bishopMoves.moveHelper(myPosition, chessPieceMoves, 1, 1, board);
        bishopMoves.moveHelper(myPosition, chessPieceMoves, -1, -1, board);
        bishopMoves.moveHelper(myPosition, chessPieceMoves, 1, -1, board);
        bishopMoves.moveHelper(myPosition, chessPieceMoves, -1, 1, board);
        return chessPieceMoves;
    }
}

