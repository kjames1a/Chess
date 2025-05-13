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
        kingMovesCapture(kingPosition, chessPieceMoves, 1, 0, board);
        kingMovesCapture(kingPosition, chessPieceMoves, 1, 1, board);
        kingMovesCapture(kingPosition, chessPieceMoves, 0, 1, board);
        kingMovesCapture(kingPosition, chessPieceMoves, -1, 1, board);
        kingMovesCapture(kingPosition, chessPieceMoves, -1, 0, board);
        kingMovesCapture(kingPosition, chessPieceMoves, -1, -1, board);
        kingMovesCapture(kingPosition, chessPieceMoves, 0, -1, board);
        kingMovesCapture(kingPosition, chessPieceMoves, 1, -1, board);
        return chessPieceMoves;
    }

    private boolean outOfBounds (ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        return (row < 1 || col < 1 || row > 8 || col > 8);
    }

    private void kingMovesCapture (ChessPosition myPosition, Collection<ChessMove> chessMoves, int rowMove, int colMove, ChessBoard board) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int newRow = row + rowMove;
        int newCol = col + colMove;
        if (!outOfBounds(new ChessPosition(newRow, newCol))) {
            ChessPosition endPosition = new ChessPosition(newRow, newCol);
            ChessPiece mainTeam = board.getPiece(myPosition);
            ChessPiece oppTeam = board.getPiece(endPosition);
            if (oppTeam == null) {
                chessMoves.add(new ChessMove(myPosition, endPosition, null));
            } else {
                if (oppTeam.getTeamColor() != mainTeam.getTeamColor()) {
                    chessMoves.add(new ChessMove(myPosition, endPosition, null));
                }
            }
        }
    }
}
