package assignment;
import java.util.Random;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;
public class TreapMap<K extends Comparable<K>, V> implements Treap<K, V> {
    private TreapNode root;
    private Random r = new Random();
    private K largest;
    private long modCount = 0;
    /**
     * Retrieves the value associated with a key in this dictionary.
     * If the key is null or the key is not present in this
     * dictionary, this method returns null.
     *
     * @param key   the key whose associated value
     *              should be retrieved
     * @return	    the value associated with the key, or
     *        	    null if the key is null or the key is not in 
     *              this treap
     */
    public TreapMap(){
    }
    private TreapMap(TreapNode root){
        this.root = root;
    }
    public V lookup(K key){
        TreapNode current = root;
        while(current != null){
            int compare = current.key.compareTo(key);
            if(compare == 0)
                return current.value;
            if(compare >= 1)
                current = current.leftChild;
            else 
                current = current.rightChild;
        }
        return null;
    }

    /**
     * Adds a key-value pair to this dictionary.  Any old value
     * associated with the key is lost.  If the key or the value is
     * null, the pair is not added to the dictionary.
     *
     * @param key  	the key to add to this dictionary
     * @param value	 the value to associate with the key
     **/
    public void insert(K key, V value){
        if(root == null){
            root = new TreapNode(key, value, r.nextInt(1000000000));
            largest = key;
        }
        insert(key, value, r.nextInt(1000000000), null, root);
        modCount++;
    }

    private TreapNode insert(K key, V value, int priority, TreapNode parent, 
            TreapNode current) {
        int compare = current.key.compareTo(key);
        if(compare == 0){
            current.value = value;
            current.priority = priority;
            if(largest.compareTo(key) < 0)
                largest = key;
        }
        else if(compare > 0){
            if(current.leftChild == null)
                current.leftChild = new TreapNode(key, value, priority);
            current = insert(key, value, priority, current, current.leftChild);
        }
        else {
            if(current.rightChild == null)
                current.rightChild = new TreapNode(key, value, priority);
            current = insert(key, value, priority, current, current.rightChild);
        }

        if(parent == null) {
            root = current;
            return null;
        }
        if(parent.key.compareTo(current.key) > 0)
            parent.leftChild = current;
        else
            parent.rightChild = current;
        if((parent.priority - current.priority)>0){
            return rotate(parent, current); 
        }
        else
            return parent;
    }

    /**
     * Rotates the proper direction around parent. Decides if current is left or right child
     * and performs the appropriate rotation. Returns the new top of the subtree.
     * 
     * @param parent   the node you want to rotate around
     * @param current  the child you want to rotate to the top
     * @return         the new top of the subtree
     **/

    private TreapNode rotate(TreapNode parent, TreapNode current){
        if(parent == root)
            root = current;
        int compare = parent.key.compareTo(current.key);
        TreapNode innerChild;
        if(compare > 0) {
            innerChild = current.rightChild;
            parent.leftChild = innerChild;
            current.rightChild = parent;
            return current;
        }
        else if(compare<0){
            innerChild = current.leftChild;
            parent.rightChild = innerChild;
            current.leftChild = parent;
            return current;
        }
        else{
           // System.out.println("sameKey");
            if(parent.rightChild == current){
                innerChild = current.leftChild;
                parent.rightChild = innerChild;
                current.leftChild = parent;
                return current;
            }
            else if(parent.leftChild == current){
                innerChild = current.rightChild;
                parent.leftChild = innerChild;
                current.rightChild = parent;
                return current;
            }else
                System.out.println("error in rotate");
            return current;
        }
    }

    /**
     * Removes a key from this dictionary.  If the key is not present
     * in this dictionary, this method does nothing.  Returns the
     * value associated with the removed key, or null if the key
     * is not present.
     *
     * @param key      the key to remove
     * @return         the associated with the removed key, or null 
     */
    public V remove(K key){
        if(root == null)
            return null;
        TreapNode current = root;
        TreapNode parent = null;
        int compare;
        while((compare = current.key.compareTo(key))!=0){
            //System.out.println("searching on node "+current.key);
            parent = current;
            if(compare < 0 && current.rightChild != null)
                current = current.rightChild;
            else if(compare > 0 && current.leftChild !=null)
                current = current.leftChild;
            else
                return null;
        }
        modCount++;
        return remove(current, parent);
    }

    private V remove(TreapNode current, TreapNode parent) {
        //System.out.println("in recursion on " + current.key);
        if(parent == null)
            parent = new TreapNode(current.key, current.value, 1);
        int compare = parent.key.compareTo(current.key);
        while(current.leftChild != null || current.rightChild != null) {
            // System.out.println("Parent : " + parent.toString());
            // System.out.println("Current : " + current.toString());
            // System.out.println("rotating");
            compare = parent.key.compareTo(current.key);
            TreapNode lesserChild;
            if(current.leftChild == null)
                lesserChild = current.rightChild;
            else if(current.rightChild == null)
                lesserChild = current.leftChild;
            else
                lesserChild = current.rightChild.priority < 
                    current.leftChild.priority ? current.rightChild : 
                    current.leftChild;
            if(compare > 0) {
                parent.leftChild = rotate(current,lesserChild);
                parent = parent.leftChild;
            }else{
                parent.rightChild = rotate(current,lesserChild);
                parent = parent.rightChild;
            }

            //System.out.println("Parent : " + parent.toString());
            //System.out.println("Current : " + current.toString() +"\n");
            for(int i=0; i<1000000000; i++)
                for(int k=0;k<100000000;k++);
        }
        compare = parent.key.compareTo(current.key);
        if(current == root){
            root = null;
        }else if(compare > 0){
            parent.leftChild = null;
        }else{
            parent.rightChild = null;
        }
        return current.value;
    }

    /**
     * Splits this treap into two treaps.  The left treap should contain
     * values less than the key, while the right treap should contain
     * values greater than or equal to the key.
     *
     * @param key    the key to split the treap with
     * @return       the left treap in index 0, the right in index 1
     */
    public Treap<K, V> [] split(K key){
        Treap[] out = new TreapMap[2];
        insert(key, lookup(key), -1, null, root);
        out[0] = new TreapMap(root.leftChild);
        out[1] = new TreapMap(root.rightChild);
        return out;
    }

    /**
     * Joins this treap with another treap.  If the other treap is not
     * an instance of the implementing class, both treaps are unmodified.
     * At the end of the join, this treap will contain the result.
     * This method may destructively modify both treaps.
     *
     * @param t    the treap to join with
     */
    public void join(Treap<K, V> t){
        TreapNode temp = root;
        K tempKey;
        if(root.priority < ((TreapMap<K, V>)t).root.priority)
            tempKey = ((TreapMap<K, V>)t).root.key;
        else
            tempKey = root.key;
        root = new TreapNode(tempKey,null,-1);
        root.leftChild = temp;
        root.rightChild = ((TreapMap<K, V>)t).root;
        // System.out.println("root is " + root);
        remove(root.key);
    }

    /**
     * Melds this treap with another treap.  If the other treap is not
     * an instance of the implementing class, both treaps are unmodified.
     * At the end of the meld, this treap will contain the result.  This
     * method may destructively modify both treaps.
     *
     * @param t    the treap to meld with.  t may be modified.
     */
    public void meld(Treap<K, V> t) throws UnsupportedOperationException{
        throw new UnsupportedOperationException();
    }

    /**
     * Removes elements from another treap from this treap.  If the
     * other treap is not an instance of the implementing class,
     * both treaps are unmodified.  At the end of the difference,
     * this treap will contain no keys that were in the other
     * treap.  This method may destructively modify both treaps.
     *
     * @param t   a treap containing elements to remove from this
     *            treap.  t may be destructively modified.
     */
    public void difference(Treap<K, V> t) throws UnsupportedOperationException{
        throw new UnsupportedOperationException();
    }


    /**
     * Build a human-readable version of the treap. 
     * Each node in the treap will be represented as
     *
     *     [priority] <key, value>\n
     *
     * Subtreaps are indented one tab over from their parent for
     * printing.  This method prints out the string representations
     * of key and value using the object's toString().
     */
    public String toString(){
        return toString(root, 0); 
    }

    private String toString(TreapNode node, int indents){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < indents * 4; i++)
            sb.append(' ');
        if(node == null){
            return sb.toString() + "null\n";
        }
        return toString(node.rightChild, indents + 1 ) +
            sb.toString() + node.toString() +
            toString(node.leftChild, indents + 1);
    }

    /**
     * @return a fresh iterator that points to the first element in 
     * this Treap and iterates in sorted order. 
     */
    public Iterator<K> iterator(){
        return new TreapIterator();
    }

    /**
     * Returns the balance factor of the treap.  The balance factor
     * is the height of the treap divided by the minimum possible
     * height of a treap of this size.  A perfectly balanced treap
     * has a balance factor of 1.0.  If this treap does not support
     * balance statistics, throw an exception.
     */
    public double balanceFactor() throws UnsupportedOperationException{
        throw new UnsupportedOperationException();
    }

    private class TreapIterator 
            implements Iterator<K>{
            private TreapNode thisNode = root;
            private ArrayList<TreapNode> stack = new ArrayList<>();
            int prevChild = 0; 
            long expectedModCount = modCount;
            public TreapIterator(){
                //stack.add(null);
            }
            public boolean hasNext(){
                //return thisNode != null;
                return !thisNode.key.equals(largest) || prevChild != 2;
            }
            /*
               public K next() throws NoSuchElementException{
               if(thisNode == null)
               throw new NoSuchElementException();
               while(true){
            // go left
            if(thisNode.leftChild != null && goLeft){
            stack.add(thisNode);
            thisNode = thisNode.leftChild;
            }else{
            TreapNode temp = thisNode;
            // go right
            if(thisNode.rightChild != null){
            stack.add(thisNode);
            thisNode = thisNode.rightChild;
            goLeft = true;
            return temp.key;
            }else{
            int compare;
            do{
            if(stack.size() <= 0){
            thisNode = null;
            break;
            }
            TreapNode popped = stack.remove(
            stack.size() - 1);
            compare = popped.key.compareTo(thisNode.key);
            thisNode = popped;
            }while(compare < 0);
            goLeft = false;
            return temp.key;
            }
            }
               }
               }
             */
            public K next() throws NoSuchElementException{
                // 0 - not visited before, 1 - came from left, 2 - came from right
                if(expectedModCount != modCount)
                    throw new ConcurrentModificationException();
                TreapNode temp;
                while(true){
                    // left isn't null and this is our first time on this branch
                    if(thisNode.leftChild != null && prevChild == 0){
                        stack.add(thisNode);
                        thisNode = thisNode.leftChild;
                    }else{
                        // first time on branch (left was null)
                        if(prevChild == 0){
                            prevChild = 1;
                        }else if(prevChild == 1){ // came back from left (must return)
                            temp = thisNode;
                            if(thisNode.rightChild == null)
                                prevChild = 2;
                            else{
                                stack.add(thisNode);
                                thisNode = thisNode.rightChild;
                                prevChild = 0;
                            }
                            return temp.key;
                        }else{ // came back from right
                            temp = thisNode;
                            if(stack.size() <= 0)
                                throw new NoSuchElementException(); 
                            thisNode = stack.remove(stack.size() - 1);
                            int compare = thisNode.key.compareTo(temp.key);
                            if(compare > 0){
                                prevChild = 1;
                            }else{
                                prevChild = 2;
                            }
                        }
                    }
                }
            }
            public void remove() throws UnsupportedOperationException{
                throw new UnsupportedOperationException();
            }
    }
    private class TreapNode{
        private TreapNode leftChild;
        private TreapNode rightChild;
        private K key;
        private V value;
        private int priority;

        TreapNode(K key,V value){
            this.key = key;
            this.value = value;
        }
        TreapNode(K key, V value, int priority){
            this.key = key;
            this.value = value;
            this.priority = priority;
        }

        public String toString(){
            return "[" + priority + "] <" + key + ", " + 
                value + ">\n";
        }
    }
}
