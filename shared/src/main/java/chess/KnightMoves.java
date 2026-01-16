package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMoves extends MovesCalculator {

    public Collection<ChessMove> MoveList(ChessPosition myPosition, ChessGame.TeamColor pieceColor, ChessBoard board) {
        int[][] possibleMoves = {
                {2,1},
                {2, -1},
                {-2, 1},
                {-2, -1},
                {1, 2},
                {1, -2},
                {-1, 2},
                {-1, -2}
        };
        Collection<ChessMove> availableMoves = new ArrayList<>();

        for (int[] coordinates : possibleMoves) {
            int row = myPosition.getRow();
            int col = myPosition.getColumn();

            int newRow = row + coordinates[0];
            int newCol = col + coordinates[1];
            ChessPosition newPosition = new ChessPosition(newRow, newCol);
            if (inbounds(newPosition)) {
                if (checkSpace(board, newPosition) == null || checkSpace(board, newPosition) != pieceColor) {
                    availableMoves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }
        return availableMoves;
    }

}
