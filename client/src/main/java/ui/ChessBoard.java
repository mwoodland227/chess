package ui;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static ui.EscapeSequences.*;

public class ChessBoard {
    // board dimensions
    private static final int BOARD_SIZE_IN_SQUARES = 8;


    // padded characters
    private static final String EMPTY = " ";
//    private static final String LIGHT = SET_BG_COLOR_WHITE + EMPTY + RESET_BG_COLOR;
//    private static final String DARK = SET_BG_COLOR_DARK_GREY + EMPTY + RESET_BG_COLOR;

    private static final String ROWS = "12345678";
    private static final String WHITECOL = "abcdefgh";
    private static final String BLACKCOL = "hgfedcba";

    static void main(String[] args){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
//        drawHeaders(out);
        boolean isWhite = Boolean.parseBoolean(args[1]) ;

        ChessGame game = new ChessGame();
        drawChessBoard(out, isWhite, game, null, null);
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawHeaders(PrintStream out, boolean isWhite){
        setBlack(out);
        out.print("  ");
        String cols = isWhite ? WHITECOL : BLACKCOL;
        for (int col = 0; col < BOARD_SIZE_IN_SQUARES; ++col) {
            out.print(SET_TEXT_COLOR_GREEN);
            out.print(" " + cols.charAt(col) + " ");
            setBlack(out);
        }
        out.println();
    }


    public static void  drawChessBoard(PrintStream out, boolean isWhite, ChessGame game,
                                       ChessPosition selected, Collection<ChessMove> legalMoves){
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
            drawSquareRow(out, row, isWhite, game, selected, legalMoves);

        }
        // maybe take out this second drawHeaders call
        drawHeaders(out, isWhite);
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

    private static void drawSquareRow(PrintStream out, int row, boolean isWhite, ChessGame game,
                                      ChessPosition selected, Collection<ChessMove> legalMoves) {
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            int col = isWhite ? boardCol : BOARD_SIZE_IN_SQUARES - 1 - boardCol;

            ChessPosition position = new ChessPosition(row + 1, col + 1);

            boolean isSelected = selected != null && selected.equals(position);
            boolean isLegalMove = isLegalDestination(position, legalMoves);
//            boolean isLegalMove = false;
//            if (legalMoves != null) {
//                for (ChessMove move : legalMoves) {
//                    if (move.getEndPosition().equals(position)) {
//                        isLegalMove = true;
//                        break;
//                    }
//                }
//            }

            boolean isLight = ((row + col) % 2 != 0);

            ChessPiece piece = game.getBoard().getPiece(position);
            String pieceText = getPieceString(piece);

            setSquareColor(out, isLight, isSelected, isLegalMove);
            out.print(" ");

//            String square = isLight ? LIGHT : DARK;

//            ChessPosition position = new ChessPosition(row + 1, col + 1);

            setSquareColor(out, isLight, isSelected, isLegalMove);

            if (!pieceText.equals(EMPTY)) {
                boolean isWhitePiece = Character.isUpperCase(pieceText.charAt(0));
                out.print(isWhitePiece ? SET_TEXT_COLOR_RED : SET_TEXT_COLOR_BLUE);
                out.print(pieceText);
            } else {
                out.print(" ");
            }

            setSquareColor(out, isLight, isSelected, isLegalMove);
            out.print(" ");

            out.print(RESET_BG_COLOR);
            out.print(SET_TEXT_COLOR_BLACK);

//            out.print(square);
        }
        out.print(SET_TEXT_COLOR_GREEN);
        out.print(" " + (row + 1));
        setBlack(out);
        out.println();


//            if(isLight) {
//                out.print(SET_BG_COLOR_WHITE);
//            } else{
//                out.print(SET_BG_COLOR_BLACK);
//            }

//            if (!pieceText.equals(EMPTY)) {
//                boolean isWhitePiece = Character.isUpperCase(pieceText.charAt(0));
//                if(isWhitePiece){
//                    out.print(SET_TEXT_COLOR_RED);
//                } else{
//                    out.print(SET_TEXT_COLOR_BLUE);
//
//                }


//                out.print(pieceText);
////                out.print(SET_TEXT_COLOR_BLACK);
//            } else {
//                out.print(" ");
//            }
//
//            if (isSelected) {
//                out.print(SET_BG_COLOR_YELLOW);
//            } else if (isLegalMove) {
//                out.print(SET_BG_COLOR_GREEN);
//            } else if (isLight) {
//                out.print(SET_BG_COLOR_WHITE);
//            } else {
//                out.print(SET_BG_COLOR_DARK_GREY);
//            }
//
//            out.print(square);
//            out.print(RESET_BG_COLOR);
//            out.print(SET_TEXT_COLOR_BLACK);


    }



    private static boolean isLegalDestination(ChessPosition position, Collection<ChessMove> legalMoves) {
        if (legalMoves == null) {
            return false;
        }
        for (ChessMove move : legalMoves) {
            if (move.getEndPosition().equals(position)) {
                return true;
            }
        }
        return false;
    }

    private static void setSquareColor(PrintStream out, boolean isLight, boolean isSelected, boolean isLegalMove) {
        if (isSelected) {
            out.print(SET_BG_COLOR_YELLOW);
        } else if (isLegalMove) {
            out.print(SET_BG_COLOR_GREEN);
        } else if (isLight) {
            out.print(SET_BG_COLOR_WHITE);
        } else {
            out.print(SET_BG_COLOR_DARK_GREY);
        }
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
