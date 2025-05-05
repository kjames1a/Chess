package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMoves {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> chessMoves = new ArrayList<>();
        kingMovesCapture(myPosition, chessMoves, 1, 0, board);
        kingMovesCapture(myPosition, chessMoves, 1, 1, board);
        kingMovesCapture(myPosition, chessMoves, 0, 1, board);
        kingMovesCapture(myPosition, chessMoves, -1, 1, board);
        kingMovesCapture(myPosition, chessMoves, -1, 0, board);
        kingMovesCapture(myPosition, chessMoves, -1, -1, board);
        kingMovesCapture(myPosition, chessMoves, 0, -1, board);
        kingMovesCapture(myPosition, chessMoves, 1, -1, board);
        return chessMoves;
    }

    private boolean OutOfBounds (ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        return (row < 1 || col < 1 || row > 8 || col > 8);
    }

    private void kingMovesCapture (ChessPosition myPosition, Collection<ChessMove> chessMoves, int rowMove, int colMove, ChessBoard board) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int newRow = row + rowMove;
        int newCol = col + colMove;
        if (!OutOfBounds(new ChessPosition(newRow, newCol))) {
            ChessPosition newPos = new ChessPosition(newRow, newCol);
            ChessPiece mainTeam = board.getPiece(myPosition);
            ChessPiece oppTeam = board.getPiece(newPos);
            if (oppTeam == null) {
                chessMoves.add(new ChessMove(myPosition, newPos, null));
            } else {
                if (oppTeam.getTeamColor() != mainTeam.getTeamColor()) {
                    chessMoves.add(new ChessMove(myPosition, newPos, null));
                }
            }
        }
    }
}
