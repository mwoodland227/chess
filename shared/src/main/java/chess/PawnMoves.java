package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoves extends MovesCalculator{
    public Collection<ChessMove> frontSpace (ChessPosition position, ChessGame.TeamColor pieceColor, ChessBoard board, int direction) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();

        int col = position.getColumn();
        int row = position.getRow();
        int newRow = row + direction;

        ChessPosition newPosition = new ChessPosition(newRow, col);
        if (checkSpace(board, newPosition) == null) {
            possibleMoves.add(new ChessMove(position, newPosition, null));
        }
        return possibleMoves;
    }

    public Collection<ChessMove> checkDiagonals (ChessPosition position, ChessGame.TeamColor pieceColor, ChessBoard board, int rowDirection, int colDirection) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();

        int newCol = col + colDirection;
        int newRow = row + rowDirection;
        ChessPosition newPosition = new ChessPosition(newRow, newCol);
        if (inbounds(newPosition)) {
            if (checkSpace(board, newPosition) != null && checkSpace(board, newPosition) != pieceColor) {
                moves.add(new ChessMove(position, newPosition, null));
            }
        }
        return moves;
    }

    public Collection<ChessMove> movesList(ChessPosition myPosition, ChessGame.TeamColor pieceColor, ChessBoard board) {
        // check it's position because if it is white in row 2 it can move forward 2 - same as black in row 7
        // check space in front to see if it is empty and add that to possible moves
        // check if an enemy piece is diagonal from it's position and add that to possible moves
        Collection<ChessMove> possibleMoves = new ArrayList<>();

        if (pieceColor == ChessGame.TeamColor.BLACK) {
            int row = myPosition.getRow();
            if (row == 7) {
                //check square right in front
                Collection<ChessMove> firstSpace = frontSpace(myPosition, pieceColor, board, -1);
                possibleMoves.addAll(firstSpace);
                if (firstSpace.toArray().length == 1) {
                    // check 2 spaces in front
                    Collection<ChessMove> secondSpace = frontSpace(myPosition, pieceColor, board, -2);
                    possibleMoves.addAll(secondSpace);
                }

                Collection<ChessMove> rightDiagonal = checkDiagonals(myPosition, pieceColor, board, -1, 1);
                possibleMoves.addAll(rightDiagonal);

                Collection<ChessMove> leftDiagonal = checkDiagonals(myPosition, pieceColor, board, -1, -1);
                possibleMoves.addAll(leftDiagonal);

            } else {
                // check the space directly in front
                Collection<ChessMove> forward = frontSpace(myPosition, pieceColor, board, -1);
                possibleMoves.addAll(forward);

                // check diagonal
                Collection<ChessMove> rightDiagonal = checkDiagonals(myPosition, pieceColor, board, -1, 1);
                possibleMoves.addAll(rightDiagonal);

                Collection<ChessMove> leftDiagonal = checkDiagonals(myPosition, pieceColor, board, -1, -1);
                possibleMoves.addAll(leftDiagonal);
            }

        } else if (pieceColor == ChessGame.TeamColor.WHITE) {
            int row = myPosition.getRow();
            if (row == 2) {
                //check square right in front
                Collection<ChessMove> firstSpace = frontSpace(myPosition, pieceColor, board, 1);
                possibleMoves.addAll(firstSpace);
                if (firstSpace.toArray().length == 1) {
                    // check 2 spaces in front
                    Collection<ChessMove> secondSpace = frontSpace(myPosition, pieceColor, board, 2);
                    possibleMoves.addAll(secondSpace);
                }

                // check diagonal
                Collection<ChessMove> rightDiagonal = checkDiagonals(myPosition, pieceColor, board, 1, 1);
                possibleMoves.addAll(rightDiagonal);

                Collection<ChessMove> leftDiagonal = checkDiagonals(myPosition, pieceColor, board, 1, -1);
                possibleMoves.addAll(leftDiagonal);

            } else {
                // check the space directly in front
                Collection<ChessMove> forward = frontSpace(myPosition, pieceColor, board, 1);
                possibleMoves.addAll(forward);

                // check diagonal
                Collection<ChessMove> rightDiagonal = checkDiagonals(myPosition, pieceColor, board, 1, 1);
                possibleMoves.addAll(rightDiagonal);

                Collection<ChessMove> leftDiagonal = checkDiagonals(myPosition, pieceColor, board, 1, -1);
                possibleMoves.addAll(leftDiagonal);
            }
        }


        // promotion
        ChessPiece.PieceType[] promotions = {ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.ROOK,
                ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT};
        Collection<ChessMove> promos = new ArrayList<>();
        for (ChessMove move : possibleMoves) {
            if (pieceColor == ChessGame.TeamColor.BLACK) {
                ChessPosition endSpot = move.getEndPosition();
                int row = endSpot.getRow();
                if (row == 1) {
                    for (ChessPiece.PieceType promote : promotions) {
                        promos.add(new ChessMove(myPosition, endSpot, promote));
                    }
                }
            } else {
                ChessPosition endSpot = move.getEndPosition();
                int row = endSpot.getRow();
                if (row == 8) {
                    for (ChessPiece.PieceType promote : promotions) {
                        promos.add(new ChessMove(myPosition, endSpot, promote));
                    }
                }
            }
        }

        possibleMoves.addAll(promos);

        possibleMoves.removeIf(moves -> moves.getPromotionPiece() == null &&
                (moves.getEndPosition().getRow() == 1 || moves.getEndPosition().getRow() == 8));

        return possibleMoves;
    }
}
