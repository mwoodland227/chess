package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMoves extends MovesCalculator {
    public Collection<ChessMove> moveList(ChessPosition myPosition, ChessGame.TeamColor pieceColor, ChessBoard board) {
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

        // check spaces moving diagonal right up
        Collection<ChessMove> diagonalRightUp = diagonalMoves(myPosition, pieceColor, board, 1, 1);
        availableMoves.addAll(diagonalRightUp);

        // check spaces moving diagonal right down
        Collection<ChessMove> diagonalRightDown = diagonalMoves(myPosition, pieceColor, board, -1, 1);
        availableMoves.addAll(diagonalRightDown);

        // check spaces moving diagonal left up
        Collection<ChessMove> diagonalLeftUp = diagonalMoves(myPosition, pieceColor, board, 1, -1);
        availableMoves.addAll(diagonalLeftUp);

        // check spaces moving
        Collection<ChessMove> diagonalLeftDown = diagonalMoves(myPosition, pieceColor, board, -1, -1);
        availableMoves.addAll(diagonalLeftDown);


        return availableMoves;
    }
}
