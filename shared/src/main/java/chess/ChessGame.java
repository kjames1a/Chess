package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor teamTurn;
    private ChessBoard board;


    public ChessGame() {
        this.teamTurn = TeamColor.WHITE;
        this.board = new ChessBoard();
        this.board.resetBoard();


    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece mainPiece = board.getPiece(startPosition);
        if (mainPiece == null) {
            return null;
        }
        Collection<ChessMove> validMoves = new ArrayList<>();
        Collection<ChessMove> pieceMoves = mainPiece.pieceMoves(board, startPosition);
        for (ChessMove pieceMove : pieceMoves) {
            ChessPiece tempPiece = board.getPiece(pieceMove.getEndPosition());
            board.addPiece(pieceMove.getEndPosition(), mainPiece);
            board.addPiece(startPosition, null);
            if(!isInCheck(mainPiece.getTeamColor())){
                validMoves.add(pieceMove);
            }
            board.addPiece(startPosition, mainPiece);
            board.addPiece(pieceMove.getEndPosition(), tempPiece);
        }
        return validMoves;
    }


    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = mainKingPosition(teamColor);
        if (kingPosition == null){
            return false;
        }
        for (int row = 1; row <= 8; row++){
            for (int col = 1; col <= 8; col++){
                ChessPosition oppPosition = new ChessPosition(row, col);
                ChessPiece chessPiece = board.getPiece(oppPosition);
                if (chessPiece != null && chessPiece.getTeamColor() != teamColor){
                    Collection<ChessMove> oppMoves = chessPiece.pieceMoves(board, oppPosition);
                    for (ChessMove pieceMove : oppMoves) {
                        if(pieceMove.getEndPosition().equals(kingPosition)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    private ChessPosition mainKingPosition(TeamColor teamColor) {
        for (int row = 1; row <= 8; row++){
            for (int col = 1; col <= 8; col++){
                ChessPosition kingPosition = new ChessPosition(row, col);
                ChessPiece chessPiece = board.getPiece(kingPosition);
                if (chessPiece != null && chessPiece.getPieceType() == ChessPiece.PieceType.KING && chessPiece.getTeamColor() == teamColor){
                    return kingPosition;
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(!isInCheck(teamColor)){
            return false;
        }
        for (int row = 1; row <= 8; row++){
            for (int col = 1; col <= 8; col++){
                ChessPosition piecePosition = new ChessPosition(row, col);
                ChessPiece chessPiece = board.getPiece(piecePosition);
                if (chessPiece != null && chessPiece.getTeamColor() == teamColor){
                    Collection<ChessMove> pieceMoves = chessPiece.pieceMoves(board, piecePosition);
                    for (ChessMove pieceMove : pieceMoves){
                        ChessPiece tempPiece = board.getPiece(pieceMove.getEndPosition());
                        board.addPiece(pieceMove.getEndPosition(), chessPiece);
                        board.addPiece(piecePosition, null);
                        if (!isInCheck(teamColor)){
                            board.addPiece(piecePosition, chessPiece);
                            board.addPiece(pieceMove.getEndPosition(), tempPiece);
                            return false;
                        }
                        board.addPiece(piecePosition, chessPiece);
                        board.addPiece(pieceMove.getEndPosition(), tempPiece);
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, board);
    }
}



