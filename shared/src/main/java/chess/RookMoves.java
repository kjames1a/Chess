package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMoves {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> chessPieceMoves = new ArrayList<>();
        ChessPieceHelper rookMoves = new ChessPieceHelper();
        rookMoves.moveHelper(myPosition, chessPieceMoves, 1, 0, board);
        rookMoves.moveHelper(myPosition, chessPieceMoves, -1, 0, board);
        rookMoves.moveHelper(myPosition, chessPieceMoves, 0, 1, board);
        rookMoves.moveHelper(myPosition, chessPieceMoves, 0, -1, board);
        return chessPieceMoves;
    }
}
