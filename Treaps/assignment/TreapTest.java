package assignment;
import java.util.Iterator;

public class TreapTest{
    public static void main(String[] args){
        TreapMap<Integer, Integer> treap = randomTreap();
        Iterator<Integer> it= treap.iterator();
        while(it.hasNext())
            System.out.println(it.next());
    }
    public static TreapMap<Integer, Integer> randomTreap(){
        TreapMap<Integer, Integer> result = new TreapMap<>();
        for(int i = 0; i < 1000; i++){
            result.insert(i, i);
        }
        return result;
    }
}
