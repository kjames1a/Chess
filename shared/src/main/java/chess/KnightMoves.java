package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMoves {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> chessPieceMoves = new ArrayList<>();
        ChessPieceHelper knightMoves = new ChessPieceHelper();
        knightMoves.kMoveHelper(myPosition, chessPieceMoves, 2, 1, board);
        knightMoves.kMoveHelper(myPosition, chessPieceMoves, -2, 1, board);
        knightMoves.kMoveHelper(myPosition, chessPieceMoves, 2, -1, board);
        knightMoves.kMoveHelper(myPosition, chessPieceMoves, -2, -1, board);
        knightMoves.kMoveHelper(myPosition, chessPieceMoves, -1, -2, board);
        knightMoves.kMoveHelper(myPosition, chessPieceMoves, -1, 2, board);
        knightMoves.kMoveHelper(myPosition, chessPieceMoves, 1, 2, board);
        knightMoves.kMoveHelper(myPosition, chessPieceMoves, 1, -2, board);
        return chessPieceMoves;
    }
}


