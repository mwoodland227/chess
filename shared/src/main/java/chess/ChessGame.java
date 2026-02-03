package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private final ChessBoard board;
    private TeamColor currentTurn;

    public ChessGame() {

        this.board = new ChessBoard();
        this.currentTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTurn = team;
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
       Collection<ChessMove> legalMoves = new ArrayList<>();
       ChessPiece piece = getBoard().getPiece(startPosition);
       TeamColor color = piece.getTeamColor();
       Collection<ChessMove> rawMoves = piece.pieceMoves(board, startPosition);

       for(ChessMove move : rawMoves) {
           ChessBoard testBoard;
           try {
               testBoard = (ChessBoard) board.clone();
           } catch (CloneNotSupportedException e) {
               throw new RuntimeException(e);
           }
           ChessPiece.PieceType promotion = move.getPromotionPiece();
           ChessPosition endPosition = move.getEndPosition();
           testBoard.addPiece(endPosition, piece);
           if(!isInCheck(color)) {
               legalMoves.add(new ChessMove(startPosition, endPosition, promotion));
           }

       }
       return legalMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
//        TeamColor opponent = null;
//        TeamColor player = null;
        ChessPosition kingPos = null;
//        if (teamColor == TeamColor.BLACK) {
//            player = TeamColor.BLACK;
//            opponent = TeamColor.WHITE;
//        } else {
//            player = TeamColor.WHITE;
//            opponent = TeamColor.BLACK;
//        }

        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = getBoard().getPiece(position);
                TeamColor color = piece.getTeamColor();
                 if(piece.getPieceType() == ChessPiece.PieceType.KING && color == teamColor) {
                     kingPos = position;
                 }


                }

            }

        for(int row = 1; row <= 8; row++){
            for(int col = 1; col <=8; col++){
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = getBoard().getPiece(position);
                TeamColor color = piece.getTeamColor();
                if(color != teamColor){
                    Collection<ChessMove> moves = piece.pieceMoves(board, position);
                    for(ChessMove move : moves){
                        ChessPosition endSpot = move.getEndPosition();
                        if(endSpot == kingPos){
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        board.resetBoard();
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }



}
