package client;

import ui.ChessBoard;

import dataclasses.AuthData;
import dataclasses.GameData;
import dataclasses.UserData;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static ui.EscapeSequences.*;



public class Menu {
    private final ServerFacade server;
    private final PrintStream out;
    private State state = State.SIGNEDOUT;
    private String username;
    private String password;
    private String email;
    private String authToken;
    private List<GameData> lastGames;

    public Menu(String serverUrl){
        server = new ServerFacade(serverUrl);
        this.out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    }

    public void readEval() {
        System.out.println("Welcome to Chess. Type help to start.");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while(!result.equals("quit")){
            printPrompt();
            String line = scanner.nextLine();

            result = eval(line);
            System.out.print(SET_TEXT_COLOR_BLUE + result);
        }


    }

    private void printPrompt(){
        System.out.print("\n" + ">>> " + SET_TEXT_COLOR_GREEN);
    }

    public String eval(String input){
        try{
            String[] tokens = input.toLowerCase().split(" ");
            if(tokens.length == 0) {
                return help();
            }
            String cmd = tokens[0];
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);

            return switch (state) {
                case SIGNEDOUT -> switch (cmd){
                    case "login" -> login(params);
                    case "register" -> register(params);
                    case "help" -> help();
                    case "quit" -> "quit";
                    default -> help();
                };
                case SIGNEDIN -> switch (cmd) {
                    case "creategame" -> createGame(params);
                    case "listgames" -> listGames();
                    case "playgame" -> play(params);
                    case "observe" -> observe(params);
                    case "logout" -> logout();
                    case "help" -> help();
                    case "quit" -> "quit";
                    default -> help();
                };
            };
        } catch (ClientException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String login(String... params) throws ClientException {
        if(params.length == 2){
            username = params[0];
            password = params[1];

            AuthData auth = server.login(username, password);
            authToken = auth.authToken();

            state = State.SIGNEDIN;

            return "Logged in as " + username;
        }
        throw new ClientException("Expected <username> <password>");
    }

    public String logout() throws ClientException {
        if(authToken != null){
            server.logout(authToken);
        }
        state = State.SIGNEDOUT;
        username = null;
        password = null;
        return "Logged out";
    }

    public String register(String... params) throws ClientException{
        if (params.length == 3){
            username = params[0];
            password = params[1];
            email = params[2];
            UserData user = server.register(username, password, email);
            AuthData auth = server.login(username, password);
            authToken = auth.authToken();
            state = State.SIGNEDIN;

            return "User " + user.username() + " registered";
        }
        throw new ClientException("Expected <username> <password> <email>");
    }

    public String help(){
        return switch (state){
            case SIGNEDOUT -> """
                    - register <username> <password> <email>
                    - login <username> <password>
                    - help
                    - quit
                    """;
            case SIGNEDIN -> """
                    - creategame <gameName>
                    - listgames
                    - playgame <gameID> <color, white or black>
                    - observe <gameID>
                    """;
        };
    }

    public String createGame(String... params) throws ClientException{
        if(params.length == 1){
            String gameName = params[0];
            GameData game = server.createGame(authToken, gameName);
            return "Game: " + gameName + " (ID: " +game.gameID() + ") created.";
        }
        throw new ClientException("Expected <gameName>");
    }

    public String listGames() throws ClientException {
        lastGames = server.listGames(authToken);
        if(lastGames.isEmpty()){
            return "No games available.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("List of games: \n");
        for(int i = 0; i < lastGames.size(); i++){
            GameData game = lastGames.get(i);
            sb.append(" ").append(i+1).append(". ").append(game.gameName()).append(" (White: ");
            if(game.whiteUsername() != null){
                sb.append(game.whiteUsername());
            } else{
                sb.append("Unassigned");
            }

            sb.append(", Black: ");
            if(game.blackUsername() != null) {
                sb.append(game.blackUsername());
            } else {
                sb.append("Unassigned");
            }
            sb.append(")\n");
        }
        return sb.toString();
    }

    public String play(String... params) throws ClientException{
        if (params.length == 2){
            int id = Integer.parseInt(params[0]) - 1;
            if(lastGames == null || id < 0 || id >= lastGames.size()){
                throw new ClientException("list games first");
            }

            int gameID = lastGames.get(id).gameID();
            String color = params[1].toUpperCase();

            if(!color.equals("WHITE") && !color.equals("BLACK")){
                throw new ClientException("Color must be black or white.");
            }
            server.joinGame(authToken, gameID, color);
            boolean isWhiteView = color.equals("WHITE");
            showBoard(isWhiteView);
            return "Joined game " + gameID + " as " + color;

        }
        throw new ClientException("Expected <gameIndex> <WHITE|BLACK>");
    }

    public void showBoard(boolean isWhite){
        ChessBoard.drawChessBoard(out, isWhite);
    }

    public String observe(String... params) throws ClientException{
        if(params.length == 1){
            int id = Integer.parseInt(params[0]) - 1;
            if(lastGames == null || id < 0 || id >= lastGames.size()){
                throw new ClientException("bad gameID");
            }
            showBoard(true);
            return "Observing " + id;
        }
        throw new ClientException("Expected <gameIndex>");
    }

}
