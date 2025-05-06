package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoves {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> chessMoves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessPiece mainTeam = board.getPiece(myPosition);
        singleMove(myPosition, chessMoves, board);
        if (mainTeam.getTeamColor() == ChessGame.TeamColor.BLACK) {
            pawnCaptureEnemy(myPosition, chessMoves, -1, 1, board);
            pawnCaptureEnemy(myPosition, chessMoves, -1, -1, board);
        } else {
            pawnCaptureEnemy(myPosition, chessMoves, 1, 1, board);
            pawnCaptureEnemy(myPosition, chessMoves, 1, -1, board);
        }
        if (row == 7 && mainTeam.getTeamColor() == ChessGame.TeamColor.BLACK) {
            int moveTwoRow = row - 2;
            ChessPosition targetPosition = new ChessPosition(moveTwoRow, col);
            ChessPosition nextPosition = new ChessPosition(row - 1, col);
            if (board.getPiece(targetPosition) == null && board.getPiece(nextPosition) == null) {
                chessMoves.add(new ChessMove(myPosition, targetPosition, null));
            }
        } else if (row == 2 && mainTeam.getTeamColor() == ChessGame.TeamColor.WHITE) {
            int moveTwoRow = row + 2;
            ChessPosition targetPosition = new ChessPosition(moveTwoRow, col);
            ChessPosition nextPosition = new ChessPosition(row + 1, col);
            if (board.getPiece(targetPosition) == null && board.getPiece(nextPosition) == null) {
                chessMoves.add(new ChessMove(myPosition, targetPosition, null));
            }
        }
        return chessMoves;
    }

    private boolean OutOfBounds(ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        return (row < 1 || col < 1 || row > 8 || col > 8);
    }

    private void singleMove(ChessPosition myPosition, Collection<ChessMove> chessMoves, ChessBoard board) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessPiece mainTeam = board.getPiece(myPosition);
        int rowMove;
        if (mainTeam.getTeamColor() == ChessGame.TeamColor.WHITE) {
            rowMove = row + 1;
        } else {
            rowMove = row - 1;
        }
        ChessPosition targetPosition = new ChessPosition(rowMove, col);
        if (!OutOfBounds(targetPosition) && board.getPiece(targetPosition) == null) {
            if (mainTeam.getTeamColor() == ChessGame.TeamColor.WHITE && rowMove == 8) {
                promotePawn(myPosition, targetPosition, chessMoves);
            } else if (mainTeam.getTeamColor() == ChessGame.TeamColor.BLACK && rowMove == 1) {
                promotePawn(myPosition, targetPosition, chessMoves);
            } else {
                chessMoves.add(new ChessMove(myPosition, targetPosition, null));
            }
        }
    }

    private void promotePawn (ChessPosition myPosition, ChessPosition promotePosition, Collection<ChessMove> chessMoves) {
        chessMoves.add(new ChessMove(myPosition, promotePosition, ChessPiece.PieceType.QUEEN));
        chessMoves.add(new ChessMove(myPosition, promotePosition, ChessPiece.PieceType.ROOK));
        chessMoves.add(new ChessMove(myPosition, promotePosition, ChessPiece.PieceType.KNIGHT));
        chessMoves.add(new ChessMove(myPosition, promotePosition, ChessPiece.PieceType.BISHOP));
    }

    private void pawnCaptureEnemy (ChessPosition myPosition, Collection<ChessMove> chessMoves, int rowMove, int colMove, ChessBoard board) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int newRow = row + rowMove;
        int newCol = col + colMove;
        ChessPiece mainTeam = board.getPiece(myPosition);
        ChessPosition targetPosition = new ChessPosition(newRow, newCol);
        if (!OutOfBounds(targetPosition)) {
            ChessPiece oppTeam = board.getPiece(targetPosition);
            if (oppTeam != null && oppTeam.getTeamColor() != mainTeam.getTeamColor())
                if (mainTeam.getTeamColor() == ChessGame.TeamColor.WHITE && newRow == 8) {
                promotePawn(myPosition, targetPosition, chessMoves);
            } else if (mainTeam.getTeamColor() == ChessGame.TeamColor.BLACK && newRow == 1) {
                promotePawn(myPosition, targetPosition, chessMoves);
            } else {
                    chessMoves.add(new ChessMove(myPosition, targetPosition, null));
                }
            }
        }
    }
