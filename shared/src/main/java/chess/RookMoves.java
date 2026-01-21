package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMoves extends MovesCalculator{


    public Collection<ChessMove> moveList(ChessPosition myPosition, ChessGame.TeamColor pieceColor, ChessBoard board) {
        //        int row = myPosition.getRow();
//        int col = myPosition.getColumn();


        // check spaces moving up
        Collection<ChessMove> moveUp = verticalMoves(myPosition, pieceColor, board, 1);
        Collection<ChessMove> availableMoves = new ArrayList<>(moveUp);



        // check spaces moving down
        Collection<ChessMove> moveDown = verticalMoves(myPosition, pieceColor, board, -1);
        availableMoves.addAll(moveDown);


        // check spaces moving right
        Collection<ChessMove> moveRight = horizontalMoves(myPosition, pieceColor, board, 1);
        availableMoves.addAll(moveRight);


        // check spaces moving left
        Collection<ChessMove> moveLeft = horizontalMoves(myPosition, pieceColor, board, -1);
        availableMoves.addAll(moveLeft);

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
