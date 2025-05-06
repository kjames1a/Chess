package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMoves {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> chessPieceMoves = new ArrayList<>();
        bishopCaptureEnemy(myPosition, chessPieceMoves, 1, 1, board);
        bishopCaptureEnemy(myPosition, chessPieceMoves, -1, -1, board);
        bishopCaptureEnemy(myPosition, chessPieceMoves, 1, -1, board);
        bishopCaptureEnemy(myPosition, chessPieceMoves, -1, 1, board);
        return chessPieceMoves;
    }

    private boolean OutOfBounds (ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        return (row < 1 || col < 1 || row > 8 || col > 8);
    }

    private void bishopCaptureEnemy (ChessPosition myPosition, Collection<ChessMove> chessMoves, int rowMove, int colMove, ChessBoard board) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int newRow = row + rowMove;
        int newCol = col + colMove;
        while (!OutOfBounds(new ChessPosition(newRow, newCol))) {
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

