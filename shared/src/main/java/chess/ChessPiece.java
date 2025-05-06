package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */


    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece positionPiece = board.getPiece(myPosition);
        PieceMoveCalc pieceValidMoves = new PieceMoveCalc();
        Collection<ChessMove> validMoves = null;
        if (PieceType.KNIGHT == positionPiece.getPieceType()) {
            validMoves = pieceValidMoves.knightPieceMoves(board, myPosition);
        }
        if (PieceType.KING == positionPiece.getPieceType()) {
            validMoves = pieceValidMoves.kingPieceMoves(board, myPosition);
        }
        if (PieceType.QUEEN == positionPiece.getPieceType()) {
            validMoves = pieceValidMoves.queenPieceMoves(board, myPosition);
        }
        if (PieceType.ROOK == positionPiece.getPieceType()) {
            validMoves = pieceValidMoves.rookPieceMoves(board, myPosition);
        }
        if (PieceType.PAWN == positionPiece.getPieceType()) {
            validMoves = pieceValidMoves.pawnPieceMoves(board, myPosition);
        }
        if (PieceType.BISHOP == positionPiece.getPieceType()) {
            validMoves = pieceValidMoves.bishopPieceMoves(board, myPosition);
        }
        return validMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}