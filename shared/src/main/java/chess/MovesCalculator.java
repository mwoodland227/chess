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

    public Collection<ChessMove> kingKnightMoves (int[][]possibleMoves, ChessPosition position, ChessBoard board, ChessGame.TeamColor pieceColor) {
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
        return availableMoves;
    }

    public Collection<ChessMove> verticalMoves (ChessPosition position, ChessGame.TeamColor pieceColor, ChessBoard board, int direction) {
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

        return moves;
    }

    public Collection<ChessMove> horizontalMoves (ChessPosition position, ChessGame.TeamColor pieceColor, ChessBoard board, int direction) {
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
        return moves;
    }
}
