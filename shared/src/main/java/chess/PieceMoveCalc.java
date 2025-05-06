package chess;

import java.util.Collection;

public class PieceMoveCalc {

    public Collection<ChessMove> bishopPieceMoves(ChessBoard board, ChessPosition myPosition) {
        BishopMoves bishopValidMoves = new BishopMoves();
        Collection<ChessMove> bishopMoves = bishopValidMoves.pieceMoves(board, myPosition);
        return bishopMoves;
    }

    public Collection<ChessMove> kingPieceMoves(ChessBoard board, ChessPosition myPosition) {
        KingMoves kingValidMoves = new KingMoves();
        Collection<ChessMove> kingMoves = kingValidMoves.pieceMoves(board, myPosition);
        return kingMoves;
    }

    public Collection<ChessMove> QueenPieceMoves(ChessBoard board, ChessPosition myPosition) {
        QueenMoves queenValidMoves = new QueenMoves();
        Collection<ChessMove> queenMoves = queenValidMoves.pieceMoves(board, myPosition);
        return queenMoves;
    }

    public Collection<ChessMove> RookPieceMoves(ChessBoard board, ChessPosition myPosition) {
        RookMoves rookValidMoves = new RookMoves();
        Collection<ChessMove> rookMoves = rookValidMoves.pieceMoves(board, myPosition);
        return rookMoves;
    }

    public Collection<ChessMove> KnightPieceMoves(ChessBoard board, ChessPosition myPosition) {
        KnightMoves knightValidMoves = new KnightMoves();
        Collection<ChessMove> knightMoves = knightValidMoves.pieceMoves(board, myPosition);
        return knightMoves;
    }

    public Collection<ChessMove> PawnPieceMoves(ChessBoard board, ChessPosition myPosition) {
        PawnMoves pawnValidMoves = new PawnMoves();
        Collection<ChessMove> PawnMoves = pawnValidMoves.pieceMoves(board, myPosition);
        return PawnMoves;
    }


}
