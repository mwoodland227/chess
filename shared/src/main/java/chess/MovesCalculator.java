package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MovesCalculator {

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

    public ChessMove kingKnightMoves (int[][]possibleMoves, ChessPosition position, ChessBoard board, ChessGame.TeamColor pieceColor) {
        Collection<ChessMove> availableMoves = new ArrayList<>();

        for (int[] coordinates : possibleMoves){
            int row = position.getRow();
            int col = position.getColumn();

            int newRow = row + coordinates[0];
            int newCol = col + coordinates[1];
            ChessPosition newPosition = new ChessPosition(newRow, newCol);

            if (inbounds(newPosition)) {
                if (checkSpace(board, newPosition) == null || checkSpace(board, newPosition) != pieceColor) {
                    availableMoves.add(new ChessMove(position, newPosition, null));
                }
            }
        }
        return (ChessMove) availableMoves;
    }
}
