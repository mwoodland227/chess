package ui;
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
        String COLS = iSWhite ? WHITECOL : BLACKCOL;
        for (int col = 0; col < BOARD_SIZE_IN_SQUARES; ++col) {
            out.print(SET_TEXT_COLOR_GREEN);
            out.print(" " + COLS.charAt(col) + "  ");
            setBlack(out);
        }
        out.println();
    }


    public static void  drawChessBoard(PrintStream out, boolean isWhite){
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
            drawSquareRow(out, row);

        }
    }

    private static void drawRowName(PrintStream out, char name){
        out.print(SET_TEXT_COLOR_GREEN);
        out.print(name + " ");
        setBlack(out);
    }

    private static String getPiece(int row, int col){
        if(row == 0 || row == 7){
            return switch (col){
                case 0, 7 -> "R";
                case 1, 6 -> "N";
                case 2,5 -> "B";
                case 3 -> "Q";
                case 4 -> "K";
                default -> EMPTY;
            };
        }
        if(row == 1 || row == 6){
            return "P";
        }

        return EMPTY;

    }

    private static void drawSquareRow(PrintStream out, int row){
        for (int col = 0; col < BOARD_SIZE_IN_SQUARES; ++col) {
            boolean isLight = ((row + col) % 2 == 0);
            String square;
            if(isLight) {
                square = LIGHT;
            } else{
                square = DARK;
            }
            String piece = getPiece(row, col);

            out.print(square);
            if (!piece.equals(EMPTY)) {
                out.print(SET_TEXT_COLOR_RED);
                out.print(piece);
                out.print(SET_TEXT_COLOR_BLACK);
            } else {
                out.print(" ");
            }
            out.print(square);

            if (col < BOARD_SIZE_IN_SQUARES - 1) {
                out.print("│");
            }
        }
        out.println();
    }


    private static void setBlack(PrintStream out){
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

}
