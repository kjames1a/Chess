package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMoves {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> chessPieceMoves = new ArrayList<>();
        rookMovesCapture(myPosition, chessPieceMoves, 1, 0, board);
        rookMovesCapture(myPosition, chessPieceMoves, -1, 0, board);
        rookMovesCapture(myPosition, chessPieceMoves, 0, 1, board);
        rookMovesCapture(myPosition, chessPieceMoves, 0, -1, board);
        return chessPieceMoves;
    }

    private boolean outOfBounds (ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        return (row < 1 || col < 1 || row > 8 || col > 8);
    }

    private void rookMovesCapture (ChessPosition myPosition, Collection<ChessMove> chessMoves, int rowMove, int colMove, ChessBoard board) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int newRow = row + rowMove;
        int newCol = col + colMove;
        while (!outOfBounds(new ChessPosition(newRow, newCol))) {
            ChessPosition endPosition = new ChessPosition(newRow, newCol);
            ChessPiece mainTeam = board.getPiece(myPosition);
            ChessPiece oppTeam = board.getPiece(endPosition);
            if (oppTeam == null) {
                chessMoves.add(new ChessMove(myPosition, endPosition, null));
            } else {
                if (oppTeam.getTeamColor() != mainTeam.getTeamColor()) {
                    chessMoves.add(new ChessMove(myPosition, endPosition, null));
                }
                break;
            }
            newRow += rowMove;
            newCol += colMove;
        }
    }
}
