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
    private ChessBoard board;
    private TeamColor currentTurn;

    public ChessGame() {

        this.board = new ChessBoard();
        board.resetBoard();
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
        ChessBoard holdBoard = board;
       Collection<ChessMove> legalMoves = new ArrayList<>();
       ChessPiece piece = board.getPiece(startPosition);
        if(piece == null) {
            return new ArrayList<>();
        }
       TeamColor color = piece.getTeamColor();
       Collection<ChessMove> rawMoves = piece.pieceMoves(board, startPosition);

       for(ChessMove move : rawMoves) {
           ChessBoard testBoard;
           try {
               testBoard = (ChessBoard) board.clone();
               board = testBoard;
           } catch (CloneNotSupportedException e) {
               throw new RuntimeException(e);
           }
           ChessPiece.PieceType promotion = move.getPromotionPiece();
           ChessPosition endPosition = move.getEndPosition();
           testBoard.addPiece(endPosition, piece);
           testBoard.addPiece(startPosition, null);
           if(!isInCheck(color)) {
               legalMoves.add(new ChessMove(startPosition, endPosition, promotion));
           }
           board = holdBoard;

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
        ChessPosition startPosition = move.getStartPosition();

        Collection<ChessMove> validMoves = validMoves(startPosition);
        for(ChessMove valid : validMoves) {
            if(valid.equals(move)) {
                ChessPiece piece = board.getPiece(startPosition);
                TeamColor playerColor = piece.getTeamColor();


                ChessPosition endPosition = move.getEndPosition();
                ChessPiece.PieceType promotionPiece = move.getPromotionPiece();
                ChessPiece promotion = new ChessPiece(piece.getTeamColor(), promotionPiece);
                if(promotionPiece != null){
                    board.addPiece(endPosition, piece);
                } else {
                    board.addPiece(endPosition, promotion);
                }
                board.addPiece(startPosition, null);
                return;
            }
        }

        throw new InvalidMoveException("Invalid move");

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPos = null;

        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                if(piece != null) {
                    TeamColor color = piece.getTeamColor();
                    if(piece.getPieceType() == ChessPiece.PieceType.KING && color == teamColor) {
                        kingPos = position;
                    }
                }
            }
        }

        for(int row = 1; row <= 8; row++){
            for(int col = 1; col <=8; col++){
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                if(piece != null) {
                    TeamColor color = piece.getTeamColor();
                    if(color != teamColor){
                        Collection<ChessMove> moves = piece.pieceMoves(board, position);
                        for(ChessMove move : moves){
                            ChessPosition endSpot = move.getEndPosition();
                            if(endSpot.equals(kingPos)){
                                return true;
                            }
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



}
