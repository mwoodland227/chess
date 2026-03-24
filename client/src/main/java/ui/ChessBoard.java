package ui;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class ChessBoard {
    // board dimensions
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;

    // padded characters
    private static final String EMPTY = " ";
    private static final String LIGHT = SET_BG_COLOR_WHITE + EMPTY + RESET_BG_COLOR;
    private static final String DARK = SET_BG_COLOR_DARK_GREY + EMPTY + RESET_BG_COLOR;

    private static final String ROWS = "12345678";
    private final String COLS = "abcdefgh";

    public static void main(String[] args){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        drawHeaders(out);
        drawChessBoard(out, true);
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawHeaders(PrintStream out){
        setBlack(out);
        drawColHeaders(out);
        out.println();
    }

    private static void drawColHeaders(PrintStream out){
        out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS /2));
        out.print("   ");

        for(int col = 0; col < BOARD_SIZE_IN_SQUARES; ++col){
            char c = COLS.charAt(col);
            drawColHeaders(out, c);

            if(col < BOARD_SIZE_IN_SQUARES -1){
                out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
            }
        }
    }
}
