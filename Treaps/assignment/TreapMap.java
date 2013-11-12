package assignment;
import java.util.Random;


public class TreapMap<K extends Comparable<K>, V> implements Treap {
    private TreapNode<K,V> root;
    private Random r = new Random();

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
    V lookup(K key){
        TreapNode<K,V> current = root;
        while(current!=null){
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
    void insert(K key, V value){
        if(root == null)
            root = new TreapNode(key,value);
        else
            insert(key,value,null,root);
    }

    private TreapNode<K,V> insert(K key, V value, TreapNode<K,V> parent, TreapNode<K,V> current) {
        int compare = current.key.compareTo(key);
        if(compare == 0){
            current.value = value;
        }

        else if(compare > 0){
            if(current.leftChild==null)
                current.leftChild = new TreapNode(key,value);
            current = insert(key,value,current,current.leftChild);
        }

        else {
            if(current.rightChild == null)
                current.rightChild = new TreapNode(key,value);
            current = insert(key,value,current,current.rightChild);
        }

        if(parent == null) {
            root = current;
            return null;
        }

        compare = parent.key.compareTo(current.key);
        TreapNode<K,V> innerChild;
        // if current node is left child of parent
        if(compare > 0) {
            parent.leftChild = current;
            innerChild = current.rightChild;
        }

        else {
            parent.rightChild = current;
            innerChild = current.leftChild;
        }

        if((parent.priority - current.priority)>0){
            if(compare > 0){
                parent.leftChild = innerChild;
                current.rightChild = parent;
            }
            else{
                parent.rightChild = innerChild;
                current.leftChild = parent;
            }
            return current;
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

    TreapNode<K,V> rotate(TreapNode<K,V> parent, TreapNode<K,V> current){
        int compare = parent.key.compareTo(current.key);
        TreapNode<K,V> innerChild;
        if(compare > 0) {
            innerChild = current.rightChild;
            parent.leftChild = innerChild;
            current.rightChild = parent;
            return current;
        }
        else {
            innerChild = current.leftChild
            parent.rightChild = innerChild;
            current.leftChild = parent;
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
    V remove(K key){

    }

    /**
     * Splits this treap into two treaps.  The left treap should contain
     * values less than the key, while the right treap should contain
     * values greater than or equal to the key.
     *
     * @param key    the key to split the treap with
     * @return       the left treap in index 0, the right in index 1
     */
    Treap<K, V> [] split(K key);

    /**
     * Joins this treap with another treap.  If the other treap is not
     * an instance of the implementing class, both treaps are unmodified.
     * At the end of the join, this treap will contain the result.
     * This method may destructively modify both treaps.
     *
     * @param t    the treap to join with
     */
    void join(Treap<K, V> t);

    /**
     * Melds this treap with another treap.  If the other treap is not
     * an instance of the implementing class, both treaps are unmodified.
     * At the end of the meld, this treap will contain the result.  This
     * method may destructively modify both treaps.
     *
     * @param t    the treap to meld with.  t may be modified.
     */
    void meld(Treap<K, V> t) throws UnsupportedOperationException{
    throw new Unsupported
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
    void difference(Treap<K, V> t) throws UnsupportedOperationException;


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
    String toString();


    /**
     * @return a fresh iterator that points to the first element in 
     * this Treap and iterates in sorted order. 
     */
    Iterator<K> iterator();

    /**
     * Returns the balance factor of the treap.  The balance factor
     * is the height of the treap divided by the minimum possible
     * height of a treap of this size.  A perfectly balanced treap
     * has a balance factor of 1.0.  If this treap does not support
     * balance statistics, throw an exception.
     */
    double balanceFactor() throws UnsupportedOperationException;

    private class TreapNode<K extends Comparable<K>, V> {
        private TreapNode<K,V> leftChild;
        private TreapNode<K,V> rightChild;
        private K key;
        private V value;
        private int priority;

        public TreapNode(K key,V value){
            this.key = key;
            this.value = value;
            priority = r.nextInt();
        }

    }

}
