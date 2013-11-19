package assignment;
import java.util.Iterator;
import java.util.Random;

public class TreapTest{
    public static void main(String[] args){ 
        TreapMap<Integer, Integer> treap = randomTreap(0, 50);
        TreapMap<Integer, Integer> treap2 = randomTreap(51, 101);
        sizeTest(treap, 50);
        sizeTest(treap2, 50);
        lookupTest(treap, 0, 50);
        orderTest(treap);
        joinTest(treap,treap2);
        for(int i = 10; i < 20; i++)
            treap.remove(i);
        System.out.println("removed 10-19");
        sizeTest(treap, 90);
        lookupTest(treap, 0, 10);
        lookupTest(treap, 20, 50);
        orderTest(treap);
        /*
        TreapMap<Integer, Integer> t = randomTreap(0, 10);
        TreapMap<Integer, Integer> t1 = randomTreap(10, 20);
        System.out.printf("t %s\n", t.toString());
        System.out.printf("t1 %s\n", t1.toString());
        t.join(t1);
        System.out.printf("Joined %s\n", t.toString());
        */
    }

    public static TreapMap<Integer, Integer> randomTreap(int s, int f){
        TreapMap<Integer, Integer> result = new TreapMap<>();
        Random r = new Random();
        for(int i = s; i < f; i++){
            result.insert(i, i);
        }
        System.out.println("created treap");
        return result;
    }

    public static void sizeTest(TreapMap map, int expected){
        Iterator it = map.iterator();
        int i = 0;
        while(it.hasNext()) {
            it.next();
            i++;
        }
        if(expected == i)
            System.out.println("Treap is expected size");
        else
            System.out.println("Treap is not expected size " + i + "\n" + map);
    }

    public static void lookupTest(TreapMap map, int s, int f){
        boolean right = true;
        for(int i = s; i < f; i++)
            right = right && (map.lookup(i) == i);
        if(right)
            System.out.println("lookup and key-value pairs are correct");
        else
            System.out.println("lookup and key-value pairs are incorrect");
    }

    public static void orderTest(TreapMap map){
        Iterator<Integer> it = map.iterator();
        int prev = it.next();
        boolean ordered = true;
        while(it.hasNext() && ordered){
            int next = it.next();
            ordered = ordered && (next > prev);
        }
        if(ordered)
            System.out.println("Iterator goes in order of keys");
        else
            System.out.println("Iterator doesn't go in order of keys");
    }

    public static void joinTest(TreapMap map1, TreapMap map2){
        System.out.println("Joining Treaps");
        map1.join(map2);
        System.out.println("Treaps joined");
        System.out.print("Joined treap: ");
        sizeTest(map1,100);
        System.out.print("Joined treap: ");
        orderTest(map1);
    }
}
