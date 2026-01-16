package chess;

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
}
