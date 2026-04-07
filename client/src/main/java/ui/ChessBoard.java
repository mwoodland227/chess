package ui;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class ChessBoard {
    // board dimensions
    private static final int BOARD_SIZE_IN_SQUARES = 8;


    // padded characters
    private static final String EMPTY = " ";
    private static final String LIGHT = SET_BG_COLOR_WHITE + EMPTY + RESET_BG_COLOR;
    private static final String DARK = SET_BG_COLOR_DARK_GREY + EMPTY + RESET_BG_COLOR;

    private static final String ROWS = "12345678";
    private static final String WHITECOL = "abcdefgh";
    private static final String BLACKCOL = "hgfedcba";

    static void main(String[] args){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
//        drawHeaders(out);
        boolean isWhite = Boolean.parseBoolean(args[1]) ;

        ChessGame game = new ChessGame();
        drawChessBoard(out, isWhite, game);
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawHeaders(PrintStream out, boolean iSWhite){
        setBlack(out);
        out.print("  ");
        String cols = iSWhite ? WHITECOL : BLACKCOL;
        for (int col = 0; col < BOARD_SIZE_IN_SQUARES; ++col) {
            out.print(SET_TEXT_COLOR_GREEN);
            out.print(" " + cols.charAt(col) + " ");
            setBlack(out);
        }
        out.println();
    }


    public static void  drawChessBoard(PrintStream out, boolean isWhite, ChessGame game){
        drawHeaders(out, isWhite);
        setBlack(out);
        for(int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow){
            int row;
            if(isWhite){
                row = BOARD_SIZE_IN_SQUARES - 1 - boardRow;
            } else {
                row = boardRow;
            }
            char rowName = ROWS.charAt(row);
            drawRowName(out, rowName);
            drawSquareRow(out, row, isWhite, game);

        }
    }

    private static void drawRowName(PrintStream out, char name){
        out.print(SET_TEXT_COLOR_GREEN);
        out.print(name + " ");
        setBlack(out);
    }

//    private static String getPiece(int row, int col){
//        if(row == 0){
//            return switch (col){
//                case 0, 7 -> "R";
//                case 1, 6 -> "N";
//                case 2,5 -> "B";
//                case 3 -> "Q";
//                case 4 -> "K";
//                default -> EMPTY;
//            };
//        }
//        if(row == 1){
//            return "P";
//        }
//
//        if(row == 7){
//            return switch (col){
//                case 0, 7 -> "r";
//                case 1, 6 -> "n";
//                case 2,5 -> "b";
//                case 3 -> "q";
//                case 4 -> "k";
//                default -> EMPTY;
//            };
//        }
//        if(row == 6){
//            return "p";
//        }
//
//        return EMPTY;
//
//    }
    // the old hard coded board pieces

    private static void drawSquareRow(PrintStream out, int row, boolean isWhite, ChessGame game){
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            int col = isWhite ? boardCol : BOARD_SIZE_IN_SQUARES - 1 - boardCol;

            boolean isLight = ((row + col) % 2 != 0);

            String square = isLight ? LIGHT : DARK;

            ChessPosition position = new ChessPosition(row + 1, col + 1);
            ChessPiece piece = game.getBoard().getPiece(position);
            String pieceText = getPieceString(piece);

            out.print(square);

            if(isLight) {
                out.print(SET_BG_COLOR_WHITE);
            } else{
                out.print(SET_BG_COLOR_BLACK);
            }

            if (!pieceText.equals(EMPTY)) {
                boolean isWhitePiece = Character.isUpperCase(pieceText.charAt(0));
                if(isWhitePiece){
                    out.print(SET_TEXT_COLOR_RED);
                } else{
                    out.print(SET_TEXT_COLOR_BLUE);

                }


                out.print(pieceText);
                out.print(SET_TEXT_COLOR_BLACK);
            } else {
                out.print(" ");
            }
            out.print(square);

        }
        out.println();
    }

    private static String getPieceString(ChessPiece piece) {
        if (piece == null) {
            return EMPTY;
        }

        return switch (piece.getPieceType()) {
            case KING -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? "K" : "k";
            case QUEEN -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? "Q" : "q";
            case ROOK -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? "R" : "r";
            case BISHOP -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? "B" : "b";
            case KNIGHT -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? "N" : "n";
            case PAWN -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? "P" : "p";
        };
    }


    private static void setBlack(PrintStream out){
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

}
