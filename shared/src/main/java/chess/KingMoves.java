package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMoves {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> chessPieceMoves = new ArrayList<>();
        kingMovesCapture(myPosition, chessPieceMoves, 1, 0, board);
        kingMovesCapture(myPosition, chessPieceMoves, 1, 1, board);
        kingMovesCapture(myPosition, chessPieceMoves, 0, 1, board);
        kingMovesCapture(myPosition, chessPieceMoves, -1, 1, board);
        kingMovesCapture(myPosition, chessPieceMoves, -1, 0, board);
        kingMovesCapture(myPosition, chessPieceMoves, -1, -1, board);
        kingMovesCapture(myPosition, chessPieceMoves, 0, -1, board);
        kingMovesCapture(myPosition, chessPieceMoves, 1, -1, board);
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
