package ui;
import chess.ChessGame;

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
        drawChessBoard(out, isWhite);
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
            drawSquareRow(out, row, isWhite);

        }
    }

    private static void drawRowName(PrintStream out, char name){
        out.print(SET_TEXT_COLOR_GREEN);
        out.print(name + " ");
        setBlack(out);
    }

    private static String getPiece(int row, int col){
        if(row == 0){
            return switch (col){
                case 0, 7 -> "R";
                case 1, 6 -> "N";
                case 2,5 -> "B";
                case 3 -> "Q";
                case 4 -> "K";
                default -> EMPTY;
            };
        }
        if(row == 1){
            return "P";
        }

        if(row == 7){
            return switch (col){
                case 0, 7 -> "r";
                case 1, 6 -> "n";
                case 2,5 -> "b";
                case 3 -> "q";
                case 4 -> "k";
                default -> EMPTY;
            };
        }
        if(row == 6){
            return "p";
        }

        return EMPTY;

    }

    private static void drawSquareRow(PrintStream out, int row, boolean isWhite){
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            int col = isWhite ? boardCol : BOARD_SIZE_IN_SQUARES - 1 - boardCol;

            boolean isLight = ((row + col) % 2 != 0);

            String square = isLight ? LIGHT : DARK;

            String piece = getPiece(row, col);
            out.print(square);
//            out.print(square);

            if(isLight) {
                out.print(SET_BG_COLOR_WHITE);
            } else{
                out.print(SET_BG_COLOR_BLACK);
            }

            if (!piece.equals(EMPTY)) {
                boolean isWhitePiece = Character.isUpperCase(piece.charAt(0));
                if(isWhitePiece){
                    out.print(SET_TEXT_COLOR_RED);
                } else{
                    out.print(SET_TEXT_COLOR_BLUE);

                }


                out.print(piece);
//                out.print(square);
                out.print(SET_TEXT_COLOR_BLACK);
            } else {
                out.print(" ");
            }
            out.print(square);

        }
        out.println();
    }


    private static void setBlack(PrintStream out){
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

}
