package assignment;

import java.awt.Point;
import java.util.List;
import java.util.Collection;
import java.util.Scanner;

public class Boggle{
    // ANSI Escape sequences for text formatting
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_CLEAR_SCRN = "\u001B[2J";
    public static final String ANSI_MOVE_UP_ONE = "\u001B[1A";
    public static final String ANSI_MOVE_DOWN_ONE = "\u001B[1B";
    public static final String ANSI_CLEAR_LINE = "\u001B[2K";
    public static final String ANSI_BG_BLACK = "\u001B[40m";
    public static final String ANSI_SAVE_CURSOR = "\u001B[s";
    public static final String ANSI_RESTORE_CURSOR = "\u001B[u";
    public static final String ANSI_CLEAR_BELOW = "\u001B[J";
    // location of board
    public static final int ORIGIN_LINE = 1;
    public static final int ORIGIN_COLUMN = 0;

    private BoggleDictionary d;
    private GameManager g;
    private int width, height, players, size;
    boolean playerDone;

    private Boggle(){
        d = new GameDictionary();
        d.loadDictionary("words.txt");
        g = new GameManager();
    }
    
    // prompts user for number of players and size of board.
    private void newGame(){
        boolean gameCreated = false;
        System.out.print(ANSI_CLEAR_SCRN);
        moveCursor(2, 0);
        while(!gameCreated){
            boolean validInput = false;
            while(!validInput){
                try{
                    players = Integer.parseInt(System.console().readLine(
                                "Number of Players: "));
                    validInput = true;
                }catch(NumberFormatException e){
                    System.err.println("Enter a numeral");
                }
            }
            System.out.print(ANSI_CLEAR_SCRN);
            moveCursor(2, 0);
            validInput = false;
            while(!validInput){
                try{
                    size = Integer.parseInt(System.console().readLine(
                                "Board Size: "));
                    validInput = true;
                }catch(NumberFormatException e){
                    System.err.println("Enter a numeral");
                }
            }
            System.out.print(ANSI_CLEAR_SCRN);
            try{
                g.newGame(size, players, "cubes.txt", d);
                gameCreated = true;
            }catch(Exception e){
                moveCursor(1, 0);
                System.out.println(e);
            }
        }
    }
    
    public static void main(String[] args) {
        Boggle game = new Boggle();
        game.newGame();
        int currentPlayer = 0;
        game.resetBoard(game.g.getBoard());
        game.updateScores(0);
        while(true){
            if(currentPlayer >= game.players){
                // all players have had their turn, game ends
                // computer finds all words and prints them
                // prompts for new game
                System.out.println("\n" + ANSI_RED + "All Words Missed" + 
                        ANSI_RESET);
                Collection<String> allWords = game.g.getAllWords();
                for(String s : allWords){
                    System.out.println(s);
                }
                int winner;
                System.out.printf("Player %d had the highest score of %d\n", 
                        winner = game.highScore(), 
                        game.g.getScores()[winner]);
                String in = System.console().readLine("New Game? (y/n): ");
                while(!in.equalsIgnoreCase("y") && 
                        !in.equalsIgnoreCase("n")){
                    in = System.console().readLine("Please answer (y/n): ");
                }
                if(in.equalsIgnoreCase("y")){
                    currentPlayer = 0;
                    game.newGame();
                    game.resetBoard(game.g.getBoard());
                    game.updateScores(0);
                }else{
                    break;
                }
            }
            //prompts user for new word
            String input = game.promptWord();
            if(input == null){
                // player has ended turn
                currentPlayer++;
                game.updateScores(currentPlayer);
            }
            else{
                //checks if word was valid and earns points
                int i = game.g.addWord(input, currentPlayer);
                if(i != 0){
                    game.resetBoard(game.g.getBoard());
                    game.highlight(game.g.getLastAddedWord());
                    game.updateScores(currentPlayer);
                }else
                    System.out.print("\nWord Invalid" + ANSI_MOVE_UP_ONE + 
                            "\r");
            }
        }
    }
    
    // prompts user for next word
    private String promptWord(){
        System.out.printf(ANSI_CLEAR_LINE);
        System.out.printf(ANSI_SAVE_CURSOR); 
        String result = System.console().readLine(
                "Enter next word (\"\\end\" to end turn): ");
        System.out.printf(ANSI_RESTORE_CURSOR + ANSI_MOVE_DOWN_ONE +
                ANSI_CLEAR_BELOW + ANSI_MOVE_UP_ONE);
        if(result.equals("\\end")){
            playerDone = true;
            return null;
        }
        return result;
    }
   
    // highlights last word guessed
    private void highlight(List<Point> highlight){
        System.out.print(ANSI_SAVE_CURSOR);
        char[][] board = g.getBoard();
        for(Point p : highlight){
            moveCursor(p.x + ORIGIN_LINE, p.y * 2 + ORIGIN_COLUMN + 1);
            System.out.print(ANSI_GREEN + ANSI_BG_BLACK + 
                    board[p.x][p.y] + ANSI_RESET);
        }
        System.out.print(ANSI_RESTORE_CURSOR);
    }
   
    // refreshes board
    private void resetBoard(char[][] board){
        System.out.print(ANSI_CLEAR_SCRN);
        moveCursor(ORIGIN_LINE, ORIGIN_COLUMN);
        System.out.printf("%s", ANSI_CYAN + ANSI_BG_BLACK);
        for(int i = 0; i < board.length; i++){
            moveCursor(ORIGIN_LINE + i, ORIGIN_COLUMN);
            for(int j = 0; j < board.length; j++){
                System.out.print(board[i][j] + " "); 
            }
        }
        moveCursor(ORIGIN_LINE + board.length + 1, ORIGIN_COLUMN);
        System.out.print(ANSI_RESET);
    }
    
    // moves cursor to arbitrary location in terminal
    private void moveCursor(int line, int column){
        System.out.printf("\u001B[%d;%dH", line, column);
    }
    
    // prints out player scores
    private void updateScores(int currentPlayer){
        System.out.print(ANSI_SAVE_CURSOR);
        int[] scores = g.getScores();
        for(int i = 0; i < scores.length; i++){
            moveCursor((i % size) + 1, 2 * size + 2 + i / size * 24);
            if(currentPlayer == i)
                System.out.printf("Player%d score: %s%d%s", i, ANSI_WHITE + 
                        "\u001B[41m", scores[i], ANSI_RESET);
            else
                System.out.printf("Player%d score: %d", i, scores[i]);

        }
        System.out.print(ANSI_RESTORE_CURSOR);
    }

    // finds winner
    private int highScore(){
        int[] scores = g.getScores();
        int max = 0;
        int index = 0;
        for(int i = 0; i < scores.length; i++){
            if(max < scores[i]){
                max = scores[i];
                index = i;
            }
        }
        return index;
    }
}
