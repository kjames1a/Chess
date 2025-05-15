package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMoves {
    private final ChessPosition kingPosition;

    public KingMoves(ChessPosition kingPosition){
        this.kingPosition = kingPosition;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board) {
        Collection<ChessMove> chessPieceMoves = new ArrayList<>();
        ChessPieceHelper kingMoves = new ChessPieceHelper();
        kingMoves.kMoveHelper(kingPosition, chessPieceMoves, 1, 0, board);
        kingMoves.kMoveHelper(kingPosition, chessPieceMoves, 1, 1, board);
        kingMoves.kMoveHelper(kingPosition, chessPieceMoves, 0, 1, board);
        kingMoves.kMoveHelper(kingPosition, chessPieceMoves, -1, 1, board);
        kingMoves.kMoveHelper(kingPosition, chessPieceMoves, -1, 0, board);
        kingMoves.kMoveHelper(kingPosition, chessPieceMoves, -1, -1, board);
        kingMoves.kMoveHelper(kingPosition, chessPieceMoves, 0, -1, board);
        kingMoves.kMoveHelper(kingPosition, chessPieceMoves, 1, -1, board);
        return chessPieceMoves;
    }
}
