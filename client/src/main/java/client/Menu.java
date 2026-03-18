package client;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Menu {
    private State state = State.SIGNEDOUT;
    private String username;
    private String password;

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
            username = String.join("-", params);

            return "Logged in as " + username;
        }
        return "Expected: <username> <password>";
    }

    public String logout() {
        state = State.SIGNEDOUT;
        username = null;
        return "Logged out";
    }

}
