package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMoves extends MovesCalculator{
    private ChessPosition verticalMoves (ChessPosition position, ChessGame.TeamColor pieceColor, ChessBoard board, int direction) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();


        while(true) {
            int newRow = row + direction;
            ChessPosition newPosition = new ChessPosition(newRow, col);
            if (inbounds(newPosition)) {
                if (checkSpace(board, newPosition) == null || checkSpace(board, newPosition) != pieceColor) {
                    moves.add(new ChessMove(position, newPosition, null));
                    if(checkSpace(board, newPosition) != null) {
                        break;
                    }
                } else {
                    break;
                }
            } else {
                break;
            }
            row = row + direction;
        }

        return (ChessPosition) moves;
    }

    private ChessPosition horizontalMoves (ChessPosition position, ChessGame.TeamColor pieceColor, ChessBoard board, int direction) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();

        while(true) {
            int newCol = col + direction;
            ChessPosition newPosition = new ChessPosition(row, newCol);
            if (inbounds(newPosition)) {
                if (checkSpace(board, newPosition) == null || checkSpace(board, newPosition) != pieceColor) {
                    moves.add(new ChessMove(position, newPosition, null));
                    if(checkSpace(board, newPosition) != null) {
                        break;
                    }
                } else {
                    break;
                }
            } else {
                break;
            }
            col = col + direction;
        }
        return (ChessPosition) moves;
    }

    public Collection<ChessMove> moveList(ChessPosition myPosition, ChessGame.TeamColor pieceColor, ChessBoard board) {
        Collection<ChessMove> availableMoves = new ArrayList<>();
//        int row = myPosition.getRow();
//        int col = myPosition.getColumn();


        // check spaces moving up
        Collection<ChessMove> moveUp = (Collection<ChessMove>) verticalMoves(myPosition, pieceColor, board, 1);
        availableMoves.addAll(moveUp);



        // check spaces moving down
        Collection<ChessMove> moveDown = (Collection<ChessMove>) verticalMoves(myPosition, pieceColor, board, -1);
        availableMoves.addAll(moveDown);


        // check spaces moving right
        Collection<ChessMove> moveRight = (Collection<ChessMove>) horizontalMoves(myPosition, pieceColor, board, 1);


        // check spaces moving left
        Collection<ChessMove> moveLeft = (Collection<ChessMove>) horizontalMoves(myPosition, pieceColor, board, -1);

//        // check spaces moving diagonally right up
//        while(true) {
//            int newCol = col + 1;
//            int newRow = row + 1;
//            ChessPosition newPosition = new ChessPosition(newRow, newCol);
//            if (inbounds(newPosition)) {
//                if (checkSpace(board, newPosition) == null || checkSpace(board, newPosition) != pieceColor) {
//                    availableMoves.add(new ChessMove(myPosition, newPosition, null));
//                    if(checkSpace(board, newPosition) != pieceColor) {
//                        break;
//                    }
//                } else {
//                    break;
//                }
//            } else {
//                break;
//            }
////        }
//
//        // check spaces moving diagonally left up
//        while(true) {
//            int newCol = col - 1;
//            int newRow = row + 1;
//            ChessPosition newPosition = new ChessPosition(newRow, newCol);
//            if (inbounds(newPosition)) {
//                if (checkSpace(board, newPosition) == null || checkSpace(board, newPosition) != pieceColor) {
//                    availableMoves.add(new ChessMove(myPosition, newPosition, null));
//                    if(checkSpace(board, newPosition) != pieceColor) {
//                        break;
//                    }
//                } else {
//                    break;
//                }
//            } else {
//                break;
//            }
//        }
//
//        // check spaces moving diagonally left down
//        while(true) {
//            int newCol = col - 1;
//            int newRow = row - 1;
//            ChessPosition newPosition = new ChessPosition(newRow, newCol);
//            if (inbounds(newPosition)) {
//                if (checkSpace(board, newPosition) == null || checkSpace(board, newPosition) != pieceColor) {
//                    availableMoves.add(new ChessMove(myPosition, newPosition, null));
//                    if(checkSpace(board, newPosition) != pieceColor) {
//                        break;
//                    }
//                } else {
//                    break;
//                }
//            } else {
//                break;
//            }
//        }
//
//        // check spaces moving diagonally right down
//        while(true) {
//            int newCol = col + 1;
//            int newRow = row - 1;
//            ChessPosition newPosition = new ChessPosition(newRow, newCol);
//            if (inbounds(newPosition)) {
//                if (checkSpace(board, newPosition) == null || checkSpace(board, newPosition) != pieceColor) {
//                    availableMoves.add(new ChessMove(myPosition, newPosition, null));
//                    if(checkSpace(board, newPosition) != pieceColor) {
//                        break;
//                    }
//                } else {
//                    break;
//                }
//            } else {
//                break;
//            }
//        }

        return availableMoves;
    }
}
