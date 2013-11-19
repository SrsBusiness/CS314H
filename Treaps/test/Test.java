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
        
        t = randomTreap(0,50);
        System.out.println("Test Case 10: Lookup opertaion");
        passed = true;
        it = t.iterator();
        for(int i = 0; i < 50; i++){
            if(t.lookup(i) != i) {
                passed = false;
                break;
            }
        }
        System.out.println(passed ? "Test Case 10 passed" :
                "Test Case 10 failed");
        
        //*******************************************************************
        
        t = randomTreap(0,50);
        System.out.println("Test Case 11: Iterator order test");
        passed = true;
        passed = orderTest(t);
        System.out.println(passed ? "Test Case 11 passed" :
                "Test Case 11 failed");
                
        //*******************************************************************
        
        t = randomTreap(0,50);
        System.out.println("Test Case 12a: Joining size test");
        passed = true;
        TreapMap<Integer, Integer> t2 = randomTreap(51,101);
        t.join(t2);
        passed = sizeTest(t,100);
        System.out.println(passed ? "Test Case 12a passed" :
                "Test Case 12a failed");
        
        //*******************************************************************
        
        System.out.println("Test Case 12b: Joining  order test");
        passed = true;
        passed = orderTest(t);
        System.out.println(passed ? "Test Case 12b passed" :
                "Test Case 12b failed");
         
        //*******************************************************************
        
        System.out.println("Test Case 12c: Joining lookup test");
        passed = true;
        it = t.iterator();
        for(int i = 0; i < 101; i++){
            if(i==50)
                i++;
            if(t.lookup(i) != i) {
                passed = false;
                break;
            }
        }
        System.out.println(passed ? "Test Case 12c passed" :
                "Test Case 12c failed");
           
        //*******************************************************************
        
        t = randomTreap(0,50);
        System.out.println("Test Case 13a: Remove opertaion size");
        passed = true;
        for(int i = 10; i < 20; i++){
            t.remove(i);
        }
        passed = sizeTest(t,40);
        System.out.println(passed ? "Test Case 13a passed" :
                "Test Case 13a failed");
                
        //*******************************************************************
        
        System.out.println("Test Case 13b: Remove opertaion order");
        passed = true;
        passed = orderTest(t);
        System.out.println(passed ? "Test Case 13b passed" :
                "Test Case 13b failed");
            
        //*******************************************************************
        
        System.out.println("Test Case 13c: Remove opertaion lookup");
        passed = true;
        passed = lookupTest(t,0,10) && lookupTest(t,20,50) && !lookupTest(t,10,20);
        System.out.println(passed ? "Test Case 13c passed" :
                "Test Case 13c failed");
            
        //*******************************************************************
        
        t = randomTreap(0,50);
        System.out.println("Test Case 14: Split test");
        passed = true;
        t.remove(25);
        Treap[] maps = t.split(25);
        System.out.println("Left treap :");
        if(!sizeTest((TreapMap)maps[0],25)){
        	System.out.println("wrong size");
        	passed = false; 
        }
        if(!orderTest((TreapMap)maps[0])){
        	System.out.println("wrong order");
        	passed = false;
        }
        if(!lookupTest((TreapMap)maps[0],0,25)){
        	System.out.println("wrong values");
        	passed = false;
        }
        System.out.println("Right treap :");
        if(!sizeTest((TreapMap)maps[1],24)){
        	System.out.println("wrong size");
        	passed = false; 
        }
        if(!orderTest((TreapMap)maps[1])){
        	System.out.println("wrong order");
        	passed = false;
        }
        if(!lookupTest((TreapMap)maps[1],26,50)){
        	System.out.println("wrong values");
        	passed = false;
        }
        System.out.println(passed ? "Test Case 14 passed" :
                "Test Case 14 failed");
            
        //*******************************************************************
        
        t = randomTreap(0,50);
        System.out.println("Test Case 14b: Split test key present");
        passed = true;
        maps = t.split(25);
        System.out.println("Left treap :");
        if(!sizeTest((TreapMap)maps[0],25)){
        	System.out.println("wrong size");
        	passed = false; 
        }
        if(!orderTest((TreapMap)maps[0])){
        	System.out.println("wrong order");
        	passed = false;
        }
        if(!lookupTest((TreapMap)maps[0],0,25)){
        	System.out.println("wrong values");
        	passed = false;
        }
        System.out.println("Right treap :");
        if(!sizeTest((TreapMap)maps[1],25)){
        	System.out.println("wrong size");
        	passed = false; 
        }
        if(!orderTest((TreapMap)maps[1])){
        	System.out.println("wrong order");
        	passed = false;
        }
        if(!lookupTest((TreapMap)maps[1],25,50)){
        	System.out.println("wrong values");
        	passed = false;
        }
        System.out.println(passed ? "Test Case 14b passed" :
                "Test Case 14b failed");
                
                    
        //*******************************************************************
        
        t = randomTreap(0,50);
        System.out.println("Test Case 14c: Split test above");
        passed = true;
        maps = t.split(53);
        System.out.println("Left treap :");
        if(!sizeTest((TreapMap)maps[0],50)){
        	System.out.println("wrong size");
        	passed = false; 
        }
        if(!orderTest((TreapMap)maps[0])){
        	System.out.println("wrong order");
        	passed = false;
        }
        if(!lookupTest((TreapMap)maps[0],0,50)){
        	System.out.println("wrong values");
        	passed = false;
        }
        System.out.println("Right treap :");
        if(!sizeTest((TreapMap)maps[1],0)){
        	System.out.println("wrong size");
        	passed = false; 
        }
        System.out.println(passed ? "Test Case 14c passed" :
                "Test Case 14c failed"); 
                     
        //*******************************************************************
 
        t = randomTreap(0,20);
        t2 = randomTreap(0,0);
        System.out.println("Test Case 15: Empty join");
        passed = true;
        t.join(t2);
        System.out.println("t1 join t2");
        if(!sizeTest(t,20)){
        	System.out.println("wrong size");
        	passed = false; 
        }
        if(!orderTest(t)){
        	System.out.println("wrong order");
        	passed = false;
        }
        if(!lookupTest(t,0,20)){
        	System.out.println("wrong values");
        	passed = false;
        }
        t2.join(t);
        System.out.println("t2 join t1");
        if(!sizeTest(t2,20)){
        	System.out.println("wrong size");
        	passed = false; 
        }
        if(!orderTest(t2)){
        	System.out.println("wrong order");
        	passed = false;
        }
        if(!lookupTest(t2,0,20)){
        	System.out.println("wrong values");
        	passed = false;
        }
        System.out.println(passed ? "Test Case 15 passed" :
                "Test Case 15 failed");

        //******************************************************************
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

    public static boolean sizeTest(TreapMap map, int expected){
        Iterator it = map.iterator();
        while(it.hasNext()) {
            it.next();
            expected--;
        }
        return expected == 0;
    }

    public static boolean lookupTest(TreapMap map, int s, int f){
        boolean right = true;
        for(int i = s; i < f; i++)
            right = right && (map.lookup(i) == i);
        return right;
    }

    public static boolean orderTest(TreapMap map){
        Iterator<Integer> it = map.iterator();
        int prev = it.next();
        boolean ordered = true;
        while(it.hasNext() && ordered){
            int next = it.next();
            ordered = ordered && (next > prev);
            prev = next;
        }
        return ordered;
    }

}
