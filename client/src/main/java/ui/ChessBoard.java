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
    private static final String COLS = "abcdefgh";

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
            drawColHeader(out, c);

            if(col < BOARD_SIZE_IN_SQUARES -1){
                out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
            }
        }
        out.println();
    }

    private static void drawColHeader(PrintStream out, char colName){
        int prefix = SQUARE_SIZE_IN_PADDED_CHARS/2;
        int suffix = SQUARE_SIZE_IN_PADDED_CHARS - prefix - 1;
        out.print(EMPTY.repeat(prefix));
        printColName(out, colName);
        out.print(EMPTY.repeat(suffix));
    }

    private static void printColName(PrintStream out, char name){
        out.print(SET_TEXT_COLOR_GREEN);
        out.print(name);
        setBlack(out);
    }

    public static void  drawChessBoard(PrintStream out, boolean isWhite){
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
            drawRowName(out, rowName);

            if(boardRow < BOARD_SIZE_IN_SQUARES -1){
                drawHorizontalLine(out);
                setBlack(out);
            }
        }
        drawColHeaders(out);
    }

    private static void drawRowName(PrintStream out, char name){
        out.print(SET_TEXT_COLOR_GREEN);
        out.print(" " + name + " ");
        setBlack(out);
    }

    private static String getPiece(int row, int col){
        if(row == 0){
            return switch (col){
                case 0, 7 -> WHITE_ROOK;
                case 1, 6 -> WHITE_KNIGHT;
                case 2,5 -> WHITE_BISHOP;
                case 3 -> WHITE_QUEEN;
                case 4 -> WHITE_KING;
                default -> EMPTY;
            };
        }
        if(row == 1){
            return WHITE_PAWN;
        }

        if(row == 7){
            return switch (col){
                case 0, 7 -> BLACK_ROOK;
                case 1, 6 -> BLACK_KNIGHT;
                case 2,5 -> BLACK_BISHOP;
                case 3 -> BLACK_QUEEN;
                case 4 -> BLACK_KING;
                default -> EMPTY;
            };
        }
        if(row == 6){
            return BLACK_PAWN;
        }

        return EMPTY;

    }

    private static void drawSquareRow(PrintStream out, int row){
        for(int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow){
            out.print(SET_TEXT_COLOR_GREEN);

            if(squareRow == SQUARE_SIZE_IN_PADDED_CHARS/2){
                out.print(" "+ ROWS.charAt(row)+ " ");
            } else{
                out.print("   ");
            }
            setBlack(out);

            for(int col = 0; col < BOARD_SIZE_IN_SQUARES; ++col){
                boolean isLight = ((row + col) % 2 == 0);
                String square;
                if(isLight){
                    square = LIGHT;
                } else{
                    square = DARK;
                }

                out.print(square);

                String piece = getPiece(row, col);
                out.print(piece);

                if(col < BOARD_SIZE_IN_SQUARES -1){
                    setRed(out);
                    out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
                    setBlack(out);
                }
            }
            out.println();
        }
    }

    private static void drawHorizontalLine(PrintStream out){
        int boardWidthInSpaces = BOARD_SIZE_IN_SQUARES * SQUARE_SIZE_IN_PADDED_CHARS +
                (BOARD_SIZE_IN_SQUARES-1) * LINE_WIDTH_IN_PADDED_CHARS;
        for(int lineRow = 0; lineRow < LINE_WIDTH_IN_PADDED_CHARS; ++ lineRow){
            setRed(out);
            out.print(EMPTY.repeat(boardWidthInSpaces));
            setBlack(out);
            out.println();
        }
    }

    private static void setRed(PrintStream out){
        out.print(SET_BG_COLOR_RED);
        out.print(SET_TEXT_COLOR_RED);
    }

    private static void setBlack(PrintStream out){
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

}
