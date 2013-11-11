import java.util.Random;
import java.util.Scanner;
import java.util.Collection;
import java.util.Iterator;
import java.util.HashSet;
import java.util.TreeSet;
import java.io.*;
import assignment.*;
public class TestSuite{
    public static void main(String[] args) throws IOException{
        /*File f = new File(".");
        GameDictionary d = new GameDictionary();
        d.loadDictionary("words.txt");
        GameManager g = new GameManager();
        g.newGame(1, 1, "cubes.txt", d);
        int num = 0;
        for(File temp : f.listFiles()){
            if(temp.toString().contains("board")){
                System.out.println(num);
                g.setGame(readBoard(temp.toString()));
                Collection<String> allWords = g.getAllWords();
                Iterator<String> it = allWords.iterator();
                BufferedWriter b = new BufferedWriter(new FileWriter("words" +
                            num + ".txt"));
                num++;
                if(it.hasNext()) b.write(it.next());
                while(it.hasNext())
                    b.write("\n" + it.next());
                b.flush();
                b.close();
            }
        }*/
        /*
        File f = new File(".");
        GameDictionary d = new GameDictionary();
        d.loadDictionary("words.txt");
        GameManager g = new GameManager();
        g.newGame(1, 8, "cubes.txt", d);
        int num = 0;
        File results = new File("results" + args[0]);
        results.mkdir();
        for(File temp : f.listFiles()){
            if(temp.toString().contains("board")){
                g.setGame(readBoard(temp.toString()));
                TreeSet<String> expected = new TreeSet<String>();
                File words = new File("words" + num + ".txt");
                Scanner sc = new Scanner(words);
                while(sc.hasNextLine()){
                    expected.add(sc.nextLine());
                }
                Collection<String> actual = g.getAllWords();
                TreeSet<String> actualSet = new TreeSet<String>(actual);
                System.out.println(actual.size() == actualSet.size() && 
                        actualSet.equals(expected));
                System.out.printf("expected size: %d  actual size: %d\n", 
                        expected.size(), actual.size());
                saveWords(actual, results.toString() + "/" + "words" +
                        num++ + ".txt");
            }
        }
        g.newGame(2, 1, "testcubes.txt", d);
        printBoard(g.getBoard());
        */
        GameManager g = new GameManager();
        g.newGame(2,2,"cubes.txt",new GameDictionary());
        for(int i = 0; i < 10; i++){
            g.setGame(readBoard("board" + i + ".txt"));
            saveWords(g.getAllWords(), "words_default" + i + ".txt");
        }   
    }

    // randomly generates array of English alphabetical characters
    public static char[][] generateBoard(int size){
        Random r = new Random();        
        char[][] board = new char[size][size];
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                board[i][j] = (char)(r.nextInt(26) + 'a');
            }
        }
        return board;
    }

    public static void printBoard(char[][] board){
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board.length; j++){
                System.out.printf("%c", board[i][j]);
            }
            System.out.println();
        }
    }
    public static void saveBoard(char board[][], String file) 
        throws IOException {
        BufferedOutputStream b = new BufferedOutputStream(
                new FileOutputStream(new File(file)));
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board.length; j++){
                b.write(board[i][j]);
            }
        }
        b.flush();
        b.close();
    }
    public static char[][] readBoard(String file) throws IOException {
        File f = new File(file);
        int size = (int)Math.sqrt(f.length());
        char board[][] = new char[size][size];
        BufferedInputStream b = new BufferedInputStream(
                new FileInputStream(file));
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                board[i][j] = (char)b.read();    
            }
        }
        b.close();
        return board;
    }
    public static void saveWords(Collection<String> words, String file) 
        throws IOException{
        Iterator<String> it = words.iterator();
        BufferedWriter b = new BufferedWriter(new FileWriter(new File(file)));
        if(it.hasNext()) b.write(it.next());
        while(it.hasNext()){
            b.write("\n" + it.next());
        }
        b.flush();
        b.close();
    }
}
