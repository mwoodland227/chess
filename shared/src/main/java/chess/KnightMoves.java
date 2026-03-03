package chess;

import java.util.Collection;

public class KnightMoves extends MovesCalculator {

    public Collection<ChessMove> moveList(ChessPosition myPosition, ChessGame.TeamColor pieceColor, ChessBoard board) {
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
        return kingKnightMoves(possibleMoves, myPosition, board, pieceColor);

    }

}
