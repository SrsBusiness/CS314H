import java.io.*;
import java.util.*;

public class CubeGenerator{
    public static void main(String[] args) throws IOException{
        Random r = new Random();
        int cubes = Integer.parseInt(args[0]);
        BufferedOutputStream f = new BufferedOutputStream(
                new FileOutputStream(new File("cubes1.txt")));
        for(int i = 0; i < cubes; i++){
            for(int j = 0; j < 6; j++){
                f.write(r.nextInt(26) + 'A');
            }
            f.write('\n');
        }
        f.flush();
        f.close();
    }
}
