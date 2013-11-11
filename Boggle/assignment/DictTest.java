package assignment;

import java.io.*;
import java.util.*;

public class DictTest{
    public static void main(String[] args) throws IOException{
        GameDictionary d = new GameDictionary();
        d.loadDictionary("words.txt");
        Iterator<String> i = d.iterator();
        Iterator<String> j = d.iterator();
        FileWriter f = new FileWriter(new File("words1.txt"));
        FileWriter g = new FileWriter(new File("words2.txt"));
        int words = 0;
        while(i.hasNext() && j.hasNext()){
            words++;
            f.write(i.next() + "\n");
            g.write(j.next() + "\n");
        }
        System.out.println(words);
        f.close();
        g.close();
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println(d.contains(sc.nextLine()));
        }
    }
}
