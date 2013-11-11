package assignment;

import java.io.*;
public class CreateWeights{
    public static void main(String[] args) throws IOException{
        FileWriter f = new FileWriter(new File("weights.txt"));
        for(int h = 0; h < 5; h++){
            for(int i = 0; i < NaiveBrain.WEIGHTS; i++){
                f.write(i / 10 + "\n");
            }
        }
        f.close();
    }
}
