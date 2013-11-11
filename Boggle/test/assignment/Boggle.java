package assignment;

import java.awt.Point;
import java.util.List;
import java.util.Collection;
import java.util.Scanner;

public class Boggle{
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
    private int width, height;
    boolean playerDone;

    private Boggle(){
        d = new GameDictionary();
        g = new GameManager();
    }

    public static void main(String[] args) {
        if(args.length != 2){
            throw new IllegalArgumentException("Requires 2 parameters.");
        }
        int size = Integer.parseInt(args[0]);
        int players = Integer.parseInt(args[1]);
        Boggle game = new Boggle();
        game.d.loadDictionary("words.txt");
        game.g.newGame(size, players, "cubes.txt", game.d);
        game.g.setGame(TestSuite.readBoard("board0.txt"));
        int currentPlayer = 0;
        game.resetBoard(game.g.getBoard());
        game.updateScores(0, size);
        while(true){
            if(currentPlayer >= players){
                // computer finds all words and prints them
                System.out.println();
                System.out.println(game.g.getBoard().length);
                System.out.println(game.g.getBoard()[0].length);
                Collection<String> allWords = game.g.getAllWords();
                System.out.println(allWords);
                for(String s : allWords){
                    System.out.println(s);
                }
                String in = System.console().readLine("New Game? (y/n): ");
                while(!in.equalsIgnoreCase("y") && !in.equalsIgnoreCase("n")){
                    in = System.console().readLine("Please answer (y/n): ");
                }
                if(in.equalsIgnoreCase("y")){
                    currentPlayer = 0;
                    game.g.newGame(size, players, "cubes.txt", game.d);
                    game.resetBoard(game.g.getBoard());
                    game.updateScores(0, size);
                }else{
                    break;
                }
            }
            String input = game.promptWord();
            if(input == null){
                currentPlayer++;
                game.updateScores(currentPlayer, size);
            }
            else{
                int i = game.g.addWord(input, currentPlayer);
                if(i != 0){
                    game.resetBoard(game.g.getBoard());
                    game.highlight(game.g.getLastAddedWord());
                    game.updateScores(currentPlayer, size);
                }
            }
        }
    }

    private String promptWord(){
        System.out.printf(ANSI_CLEAR_LINE);
        System.out.printf(ANSI_SAVE_CURSOR); 
        String result = System.console().readLine("Enter next word: ");
        System.out.printf(ANSI_RESTORE_CURSOR + ANSI_MOVE_DOWN_ONE +
                ANSI_CLEAR_BELOW + ANSI_MOVE_UP_ONE);
        if(result.equals("\\end")){
            playerDone = true;
            return null;
        }
        return result;
    }
    
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
    
    private void moveCursor(int line, int column){
        System.out.printf("\u001B[%d;%dH", line, column);
    }
    
    private void updateScores(int currentPlayer, int size){
        System.out.print(ANSI_SAVE_CURSOR);
        int[] scores = g.getScores();
        for(int i = 0; i < scores.length; i++){
            moveCursor(i + 1, 2 * size + 2);
            if(currentPlayer == i)
                System.out.printf("Player%d score: %s%d%s", i, ANSI_WHITE + 
                        "\u001B[41m", scores[i], ANSI_RESET);
            else
                System.out.printf("Player%d score: %d", i, scores[i]);

        }
        System.out.print(ANSI_RESTORE_CURSOR);
    }
}
