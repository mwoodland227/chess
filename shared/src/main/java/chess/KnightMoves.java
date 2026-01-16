package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMoves {

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
        Collection<ChessMove> availibleMoves = new ArrayList<>();

        for (int[] coordinates : possibleMoves) {
            int row = myPosition.getRow();
            int col = myPosition.getColumn();

            int newRow = row + coordinates[0];
            int newCol = col + coordinates[1];
            ChessPosition newPosition = new ChessPosition(newRow, newCol);
            if (inbounds(newPosition)) {
                if (checkSpace(board, newPosition) == null || checkSpace(board, newPosition) != pieceColor) {
                    availibleMoves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }
        return availibleMoves;
    }

    public ChessGame.TeamColor checkSpace(ChessBoard board, ChessPosition position) {
        if (board.getPiece(position) != null) {
            return board.getPiece(position).getTeamColor();
        }
        return null;
    }

    public boolean inbounds(ChessPosition position) {
        return 1 <= position.getRow() && position.getRow() <= 8
                && 1 <= position.getColumn() && position.getColumn() <= 8;
    }
}
