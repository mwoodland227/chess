package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMoves extends MovesCalculator{
    public Collection<ChessMove> moveList(ChessPosition myPosition, ChessGame.TeamColor pieceColor, ChessBoard board) {
        // check spaces moving diagonal right up
        Collection<ChessMove> diagonalRightUp = diagonalMoves(myPosition, pieceColor, board, 1, 1);
        Collection<ChessMove> availableMoves = new ArrayList<>(diagonalRightUp);

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
