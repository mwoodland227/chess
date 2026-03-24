package client;

import dataclasses.AuthData;
import dataclasses.GameData;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Menu {
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    private String username;
    private String password;
    private String email;
    private String authToken;

    public Menu(String serverUrl){
        server = new ServerFacade(serverUrl);
    }

    public void readEval() {
        System.out.println("Welcome to Chess. Sign in to start.");

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
                    case "play" -> play(params);
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
        throw new ClientException("Expected: <username> <password>");
    }

    public String logout() {
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

            return "User " + username + "registered";
        }
        throw new ClientException("Expected: <username> <password> <email>");
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
                    - createGame <gameName>
                    - listGames
                    - playGame <gameID> <color, white or black>
                    - observe <gameID>
                    """;
        };
    }

    public String createGame(String... params) throws ClientException{
        if(params.length == 1){
            String gameName = params[0];
            GameData game = server.createGame(authToken, gameName);
            return "Game: " + game.gameName() + " (ID: " +game.gameID() + ") created.";
        }
        throw new ClientException("Expected: <gameName>");
    }

    public String listGames() throws ClientException {
        List<GameData> games = server.listGames(authToken);
        if(games.isEmpty()){
            return "No games available.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("List of games: \n");
        for(int i = 0; i < games.size(); i++){
            GameData game = games.get(i);
            sb.append(" ").append(i+1).append(". ").append(game.gameName()).append("\n");

        }
        return sb.toString();
    }

    public String play(String... params) throws ClientException{
        if (params.length == 2){
            String color = params[1].toUpperCase();

            try{
                int gameID = Integer.parseInt(params[0]);
                if(color.equals("WHITE") || color.equals("BLACK")){
                    return "joining " + gameID +" as" + color;
                }
                throw new ClientException("Color must be WHITE or BLACK.");
            } catch (NumberFormatException e) {
                throw new ClientException("Game ID must be an integer.");
            }
        }
        throw new ClientException("Expected: <gameID> <WHITE|BLACK>");
    }

    public String observe(String... params) throws ClientException{
        if(params.length == 1){
            try{
                int gameID = Integer.parseInt(params[0]);
                return "observing " + gameID;
            } catch (NumberFormatException e){
                throw new ClientException("Game ID must be an integer.");
            }
        }
        throw new ClientException("Expected: <gameName");
    }

}
