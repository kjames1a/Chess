package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMoves {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> chessPieceMoves = new ArrayList<>();
        knightMovesCapture(myPosition, chessPieceMoves, 2, 1, board);
        knightMovesCapture(myPosition, chessPieceMoves, -2, 1, board);
        knightMovesCapture(myPosition, chessPieceMoves, 2, -1, board);
        knightMovesCapture(myPosition, chessPieceMoves, -2, -1, board);
        knightMovesCapture(myPosition, chessPieceMoves, -1, -2, board);
        knightMovesCapture(myPosition, chessPieceMoves, -1, 2, board);
        knightMovesCapture(myPosition, chessPieceMoves, 1, 2, board);
        knightMovesCapture(myPosition, chessPieceMoves, 1, -2, board);
        return chessPieceMoves;
    }

    private boolean OutOfBounds (ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        return (row < 1 || col < 1 || row > 8 || col > 8);
    }

    private void knightMovesCapture (ChessPosition myPosition, Collection<ChessMove> chessMoves, int rowMove, int colMove, ChessBoard board) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int newRow = row + rowMove;
        int newCol = col + colMove;
        if (!OutOfBounds(new ChessPosition(newRow, newCol))) {
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


