package assignment;

import java.io.*;
import java.util.*;

public class DictTest{
    public static void main(String[] args) throws IOException{
        /*
        GameDictionary d = new GameDictionary();
        d.loadDictionary("stressdict.txt");
        Random r = new Random();
        ArrayList<String> testWords = new ArrayList<String>();
        for(int i = 0; i < 1000; i++){
            StringBuilder sb = new StringBuilder();
            for(int j = 0; j < 95 + r.nextInt(); j++){
                sb.append(r.nextInt(26) + 'a');
            }
        }
        long startTime = System.nanoTime();
        for(String s : testWords){
            d.contains(s);
        }
        System.out.println((System.nanoTime() - startTime) / 1000000000.0);
        */
        
        /*
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println(d.contains(sc.nextLine()));
        }

        BufferedWriter b = new BufferedWriter(new FileWriter(
                    new File("stressdict.txt")));
        Random r = new Random();
        for(int i = 0; i < 10000000; i++){
            StringBuilder sb = new StringBuilder();
            for(int j = 0; j < r.nextInt(10) + 95; j++ ){
                sb.append(r.nextInt(26) + 'a');
            }
            b.write(sb.toString() + "\n");
        }
        b.flush();
        b.close();
        */
        
        /*
         
        */
    }
}
