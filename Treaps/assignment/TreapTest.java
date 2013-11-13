package assignment;
import java.util.Iterator;
import java.util.Random;

public class TreapTest{
    public static void main(String[] args){
        TreapMap<Integer, Integer> treap = randomTreap();
        Iterator<Integer> it= treap.iterator();
        System.out.println(treap);
        Treap<Integer, Integer>[] split = treap.split(5);
        System.out.println(treap);
        System.out.println(split[0]);
        System.out.println(split[1]);
    }
    public static TreapMap<Integer, Integer> randomTreap(){
        TreapMap<Integer, Integer> result = new TreapMap<>();
        Random r = new Random();
        for(int i = 0; i < 10; i++){
            result.insert(i,i);
        }
        return result;
    }
}
