package client;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Menu {
    private State state = State.SIGNEDOUT;
    private String username;
    private String password;
    private String email;

    public void readEval() {
        System.out.println("Welcome to Chess. Sign in to start.");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while(!result.equals("quit")){
            printPrompt();
            String line = scanner.nextLine();

            try{
                result = eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch() {}
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
        } catch () {}
    }

    public String login(String... params){
        if(params.length == 2){
            username = params[0];
            password = params[1];

            state = State.SIGNEDIN;

            return "Logged in as " + username;
        }
        return "Expected: <username> <password>";
    }

    public String logout() {
        state = State.SIGNEDOUT;
        username = null;
        password = null;
        return "Logged out";
    }

    public String register(String... params){
        if (params.length == 3){
            username = params[0];
            password = params[1];
            email = params[2];

            return "User " + username + "registered";
        }
        return "Expected: <username> <password> <email>";
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

    public String createGame(String... params){
        if(params.length == 1){
            String gameName = params[0];
            return "Game " + gameName + " created";
        }
        return "Expected: <gameName>";
    }

    public String listGames(){
        return "list of games";
    }

    public String play(String... params){
        if (params.length == 2){
            String color = params[1].toUpperCase();

            try{
                int gameID = Integer.parseInt(params[0]);
                if(color.equals("WHITE") || color.equals("BLACK")){
                    return "joining " + gameID +" as" + color;
                }
                return "Color must be WHITE or BLACK.";
            } catch ()
        }
        return "Expected: <gameID> <WHITE|BLACK>";
    }

    public String observe(String... params){
        if(params.length == 1){
            int gameID = Integer.parseInt(params[0]);
            try{
                return "observing " + gameID;
            } catch ()
        }
        return "Expected: <gameName";
    }

}
