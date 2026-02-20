package dataClasses;

import chess.ChessGame;

public record GameData(Integer gameID, String whiteUsername, String blackUsername, ChessGame game) {
}
