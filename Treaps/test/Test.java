import assignment.*;
import java.util.Iterator;
import java.util.Random;
import java.util.NoSuchElementException;

public class Test{
    public static void main(String[] args){
        testCases(); 
    }
    
    public static void testCases(){
        Random r = new Random(); 
        TreapMap<Integer, Integer> t;
        boolean passed;
        System.out.println("Test Case 1: Insert & iterator().next()"); 
        passed = true;
        t = randomTreap(0, 50);
        
        Iterator<Integer> it = t.iterator();
        try{
            for(int i = 0; i < 50; i++){
                int j;
                if((j = it.next()) != i){
                    System.out.printf("it.next() %d, expected: %d\n",
                            j, i);
                    passed = false;
                    break;
                }
            }
        }catch(NoSuchElementException | NullPointerException e){
            passed = false;
        }
        System.out.println(passed ? "Test Case 1 passed" : 
                "Test Case 1 failed");
        
        //*******************************************************************
        
        System.out.println("Test Case 2a: iterator().next() throws Exception");
        passed = true;
        try{
            System.out.println(it.next());
            passed = false;
        }catch(NoSuchElementException e){
        }
        System.out.println(passed ? "Test Case 2 passed" : 
                "Test Case 2 failed");
         
        //*******************************************************************
        
        System.out.println("Test Case 2b: iterator().hasNext() returns false");
        passed = !it.hasNext();
        System.out.println(passed ? "Test Case 2b passed" : 
                "Test Case 2b failed");

        //*******************************************************************
        
        t = randomTreap(0, 50);
        it = t.iterator();
        System.out.println("Test Case 2c: .next() consistent with .hasNext()");
        passed = true;
        try{
            for(int i = 0; i < 50; i++){
                if(!it.hasNext()){
                    passed = false;
                    break;
                }
                it.next();
            }
        }catch(NoSuchElementException e){
            passed = false;
        }
        if(it.hasNext())
            passed = false;
        try{
            it.next();
            passed = false;
        }catch(NoSuchElementException e){
            passed = true;
        }
        System.out.println(passed ? "Test Case 2c passed" : 
                "Test Case 2c failed"); 

        //*******************************************************************
        
        System.out.println("Test Case 3: Remove all with iterator().remove()");
        passed = true;
        it = t.iterator();
        try{
            while(it.hasNext()){
                it.next();
                it.remove();
            }
            it = t.iterator();
            try{
                it.next();
                passed = false;
            }catch(NoSuchElementException e){
            }
        }catch(UnsupportedOperationException e){
            System.out.println("Remove unsupported");
        }
        System.out.println(passed ? "Test Case 3 passed" : 
                "Test Case 3 failed");
        
        //*******************************************************************
        
        t = randomTreap(0, 50);
        it = t.iterator();
        System.out.println("Test Case 4: iterator().remove()");
        passed = true;
        it = t.iterator();
        try{
            it.next();
            it.remove();
            it = t.iterator();
            for(int i = 1; i < 50; i++){
                if(it.next() != i){
                    passed = false;
                    break;
                }
            }
        }catch(UnsupportedOperationException e){
            System.out.println("Remove unsupported");
        }catch(NoSuchElementException e){
            passed = false;
        }
        System.out.println(passed ? "Test Case 4 passed" : 
                "Test Case 4 failed");

        //*******************************************************************
       
        t = randomTreap(0, 50);
        it = t.iterator();
        Iterator<Integer> it2 = t.iterator();
        System.out.println("Test Case 5: multiple iterators");
        passed = true;
        if(it == it2){
            System.out.println("Singleton Iterator =(");
            passed = false;
        }
        try{
            for(int i = 0; i < 50; i++){
                int a, b = -1;
                if((a = it.next()) != i || (b = it2.next()) != i){
                    System.out.printf("it.next(): %d it2.next():" + 
                            "%d expected: %d\n", a, b, i);
                    passed = false;
                    break;
                }
            }
        }catch(NoSuchElementException e){
            passed = false;
        }
        System.out.println(passed ? "Test Case 5 passed" : 
                "Test Case 5 failed");

        //*******************************************************************
        
        t = randomTreap(0, 50);
        System.out.println("Test Case 6: Add Duplicate");
        passed = true;
        t.insert(7, 49);
        if(t.lookup(7) != 49){
            System.out.println(t.lookup(7));
            passed = false;
        }
        System.out.println(passed ? "Test Case 6 passed" : 
                "Test Case 6 failed");

        //*******************************************************************
       
        System.out.println("Test Case 7: Remove() duplicate");
        passed = true;
        t.remove(7);      
        if(t.lookup(7) != null){
            System.out.println(t.lookup(7));
            passed = false;
        }
        System.out.println(passed ? "Test Case 7 passed" : 
                "Test Case 7 failed");

        //*******************************************************************
        
        t = randomTreap(0, 50);
        System.out.println("Test Case 8: Remove() All");
        passed = true;
        try{
            for(int i = 0; i < 50; i++){
                t.remove(i);
            }
            it = t.iterator();
            try{
                int i = it.next();
                passed = false;
            }catch(NoSuchElementException e){
                passed = !it.hasNext();
            }
        }catch(NullPointerException e){
            System.out.println(e);
            passed = false;
        }
        System.out.println(passed ? "Test Case 8 passed" : 
                "Test Case 8 failed");
        
        //*******************************************************************
        
        t = randomTreap(50, 100);
        System.out.println("Test Case 9: Remove() nonexistent keys");
        passed = true;
        try{
            for(int i = 0; i < 50; i++){
                t.remove(0);
            }
            for(int i = 50; i < 100; i++){
                if(t.lookup(i) != i){
                    passed = false;
                    break;
                }
            }
        }catch(NullPointerException e){
            System.out.println(e);
            passed = false;
        }
        System.out.println(passed ? "Test Case 9 passed" : 
                "Test Case 9 failed");
         
        //*******************************************************************
        /* 
        t = randomTreap(0, 10);
        System.out.println("Test Case 10: toString()");
        passed = true;
        System.out.println(t + "\n");
        System.out.println("Added 11");
        t.insert(11, 11);
        System.out.println(t + "\n");
        System.out.println("Removed nothing (-1)");
        t.remove(-1);
        System.out.println(t + "\n");
        System.out.println("Removed 5");
        t.remove(5);
        System.out.println(t + "\n");
        System.out.println(passed ? "Test Case 10 passed" : 
                "Test Case 10 failed");
        */
        //*******************************************************************
        //*******************************************************************
        //*******************************************************************
        //*******************************************************************
        //*******************************************************************

    }
    public static TreapMap<Integer, Integer> randomTreap(int s, int f){
        TreapMap<Integer, Integer> result = new TreapMap<>();
        Random r = new Random();
        for(int i = s; i < f; i++){
            result.insert(i, i);
        }
        return result;
    }

    public static void sizeTest(TreapMap map, int expected){
        Iterator it = map.iterator();
        while(it.hasNext()) {
            it.next();
            expected--;
        }
        if(expected == 0)
            System.out.println("Treap is expected size");
        else
            System.out.println("Treap is not expected size");
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
