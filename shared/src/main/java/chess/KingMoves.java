package chess;

import java.util.Collection;

public class KingMoves extends MovesCalculator{
    public Collection<ChessMove> moveList(ChessPosition myPosition, ChessGame.TeamColor pieceColor, ChessBoard board) {
        int[][] possibleMoves = {
                {0,-1},
                {0,1},
                {-1,0},
                {-1,-1},
                {-1,1},
                {1,0},
                {1,-1},
                {1,1}
        };
        return kingKnightMoves(possibleMoves, myPosition, board, pieceColor);
    }
}