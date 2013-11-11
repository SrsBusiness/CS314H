package assignment;

import java.awt.Point;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.Random;
import java.util.*;
import java.io.*;

public class GameManager implements BoggleGame {
    private char[][] board;
    private int[] playerScores;
    private TreeSet<String> playerWords; // words guessed so far
    private BoggleDictionary dict;
    private SearchTactic tactic;
    private boolean[] turnOver;
    private List<Point> location;

    /**
      Creates a new Boggle game using a size x size board and the
      cubes specified in the file cubeFile.  If there are insufficient
      cubes in the file, exit.

      @param size The size of the Boggle board (traditionally 4)
      @param numPlayers The number of players (traditionally 2).  Must
      have at least one player.  Players are numbered from 0 to
      numPlayers - 1.
      @param cubeFile Filename of a file containing the cubes.
      @param dict The Dictionary of valid words.

     */
    
    public void newGame(int size, int numPlayers, 
            String cubeFile, BoggleDictionary newDict) {
        if(size < 2)
            throw new IllegalArgumentException(
                    "Board size should not be smaller than 2");
        if(numPlayers < 1)
            throw new IllegalArgumentException(
                    "Number of players should be at least 1");
        try(Scanner sc = new Scanner( new File(cubeFile))){
            // initializes all fields
            board = new char[size][size];
            playerScores = new int[numPlayers];
            playerWords = new TreeSet<String>();
            dict = newDict;
            tactic = SEARCH_DEFAULT;
            turnOver = new boolean[numPlayers];
            location = null;
            ArrayList<char[]> cubes = new ArrayList<char[]>();
            // read cube file: ignore cubes less than length 6
            // if the cube is longer than 6, we ignore the excess
            // if any of the 1st 6 chars aren't letters, we ignore the cube
            Random r = new Random();
            while(sc.hasNextLine()){
                String cube = sc.nextLine().toLowerCase();
                if(cube.length() < 6)
                    continue;
                if(!GameDictionary.isAlpha((cube = cube.substring(0, 6))))
                    continue;
                cubes.add(cube.toCharArray());
            }
            if(cubes.size() < size * size)
                throw new Error("Not enough cubes in cubes.txt");
            // randomize selection of cubes and the side of cube;
            for(int i=0; i<board.length; i++)
                for(int j=0; j<board.length; j++)
                    board[i][j] = Character.toLowerCase(cubes.remove
                            (r.nextInt(cubes.size()))[r.nextInt(6)]);
        }catch(FileNotFoundException e){
            throw new Error("Cubes not found");
        }
    }

    /**
      Returns a size x size character array representing the Boggle
      board, in row-major order.

     */
    public char [][] getBoard() {
        return board;
    }

    /**
      Adds a word to the player's list and returns the point value
      of the word.  If the word is invalid or the player cannot add, 
      it is worth zero points and is not actually added to the player's 
      list.  Player should not be able to add the same word multiple
      times.

      @param word The word to add.
      @param player The player who gets credit for the word.

      @return The number of points earned by this word, or zero.

     */

    public int addWord(String word, int player) {
        List<Point> temp;
        word = word.toLowerCase();
        // this.contains(String s) is a recursive call
        if(!turnOver[player] && !(playerWords.contains(word)) &&
                dict.contains(word) &&  (temp = contains(word)) != null) {
            location = temp;
            playerWords.add(word);
            playerScores[player] += word.length() - 3;
            return word.length() - 3;
        }
        return 0;
    }

    /**
      Returns a listing of coordinates (with respect to the board array)
      showing the previous successfully added word.  If there is no
      previous word, return null.

      The coordinates are listed by letter, then row, then column.
      That is, if coords is the return value, then

      coords.length is the length of the last word added
      coords[0][0] is the row coord of the first letter of the last word
      coords[0][1] is the col coord of the first letter of the last word
      coords[1][0] is the row coord of the second letter...
      ...and so on

     */

    public List<Point> getLastAddedWord() {
        return location;
    }

    /**
      Sets the game board to the given board (also in row-major
      order, sets all player scores and lists to zero/empty.  Very
      useful for debugging or playing a previous game.  The board
      must be square.  Other game-related parameters (like the
      dictionary) should be left as is.
     */

    public void setGame(char [][] newBoard) {
        board = newBoard;
        for(int i = 0; i < playerScores.length; i++)
            playerScores[i] = 0;
        playerWords.clear();
        for(int i = 0; i < turnOver.length; i++)
            turnOver[i] = false;
        location = null;
    }
    
    // iterative through the board
    private LinkedList<Point> contains(String word) {
        LinkedList<Point> temp;
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j<board.length; j++){
                if(board[i][j] == word.charAt(0) && 
                        (temp = contains(word, i, j)) != null){
                    return temp;
                }
            }
        }
        return null;
    }
    
    // recursive search through the board
    // returns the points of the word, null if it is not in the board
    private LinkedList<Point> contains(String word, int x, int y) {
        // base case
        if(word.length() <= 1) {
            LinkedList<Point> list = new LinkedList<Point>();
            list.addFirst(new Point(x,y));
            return list;
        }
        LinkedList<Point> temp;
        // marks char as visited
        board[x][y] |= 0x8000;
        // depth first search through all neighbors
        for(int i = -1; i < 2; i++){
            for(int j = -1; j < 2; j++) {
                // checks if we are off the board or visited the char already
                if(x + i >= 0 && x + i < board.length && y + j >= 0 && 
                        y + j < board.length && 
                        ((board[x + i][y + j] & 0x8000) == 0)) {
                    if(word.charAt(1) == board[x + i][y + j] &&
                            (temp=contains(word.substring(1), x + i, y + j)) != 
                            null) { 
                        board[x][y] &= ~0x8000;
                        temp.addFirst(new Point(x,y));
                        return temp;
                    }
                }  
            }
        }
        // unmarks char
        board[x][y] &= ~0x8000;
        return null;
    }

        /**
          Returns a collection containing all valid words in the current
          Boggle board.  Uses the current search tactic.

         */

    public Collection<String> getAllWords() {
        if(tactic == SearchTactic.SEARCH_DICT)
            return dictSearch();
        else
            return boardSearch();
    }

    private Collection<String> dictSearch() {
        TreeSet<String> allWords = new TreeSet<String>();
        String nextWord;
        Iterator<String> it = dict.iterator();
        // for each word in dictionary, check if it is inside the board
        while(it.hasNext()) {
            nextWord = it.next();
            if(nextWord.length() >= 4 && !playerWords.contains(nextWord) && 
                    contains(nextWord) != 
                    null)
                allWords.add(nextWord);
        }
        return allWords;
    }

    // very similar to contains()
    private Collection<String> boardSearch() {
        TreeSet<String> allWords = new TreeSet<String>();
        StringBuilder path = new StringBuilder();
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board.length; j++) {
                if(dict.isPrefix("" + board[i][j]))
                    crawler(i, j, allWords, path.append(board[i][j]));
            }
        }
        return allWords;
    }
    // recursively "crawls" around the board
    private void crawler(int x, int y, TreeSet<String> words, 
            StringBuilder path) {
        // marks current char as visited
        board[x][y] |= 0x8000;
        String s = path.toString();
        if(s.length() > 3 && !playerWords.contains(s) && dict.contains(s))
            words.add(path.toString());
        for(int i = -1; i < 2; i++)
            for(int j = -1; j < 2; j++)
                // checks if we are within board and haven't visited the next
                // char
                if(x + i >= 0 && x + i < board.length && y + j >= 0 && 
                        y + j < board.length && 
                        ((board[x + i][y + j] & 0x8000) == 0) && 
                        dict.isPrefix(path.toString() + 
                            board[x + i][y + j])
                  ) {
                    crawler(x + i, y + j, words, 
                            path.append(board[x + i][y + j]));
                }
        // resets word so far
        path.deleteCharAt(path.length() - 1);
        // unmarks current char
        board[x][y] &= ~0x8000;
    }

    /**
      Sets the search tactic (used by getAllWords()) to the given
      tactic.  Valid tactics are defined earlier.  The default tactic
      should be SEARCH_DEFAULT.  An invalid tactic resets to default.

      @param tactic The search tactic to use
     */
    public void setSearchTactic(SearchTactic newTactic) {
        tactic = newTactic;
    }

    /**
      Makes the player done with his turn.  That player can add no
      additional words.

      @param player The player who is done.
     */
    public void playerDone(int player) {
        turnOver[player] = true;
    }

    /**
      Returns the current scores for all players
     */

    public int [] getScores() {
        return playerScores;
    }
}
