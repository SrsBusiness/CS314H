package assignment;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.io.File;
import java.io.IOException;

public class GameDictionary implements BoggleDictionary{
    private GameDictionary[] nextLetters;
    private char letter; // important for iterator
    private boolean isWord;
    private GameDictionary lastNode = this;
    
    public GameDictionary(){
        nextLetters = new GameDictionary[26];
    }
     
    private GameDictionary(char c){
        this();
        letter = c;
    }
    // Iterates throught the dictionary file and adds nodes to the Trie
    public void loadDictionary(String filename){
        try(Scanner sc = new Scanner(new File(filename))){
            while(sc.hasNextLine()){
                String s = sc.nextLine().trim().toLowerCase();
                // if the word isn't purely alphabetical, skip it
                if(!isAlpha(s))
                    continue;
                // set the current node to the root
                GameDictionary thisNode = this;
                // loop creates the branch of nodes
                for(int i = 0; i < s.length(); i++){
                    if(thisNode.nextLetters[s.charAt(i) - 'a'] == null){
                        thisNode.nextLetters[s.charAt(i) - 'a'] = 
                            new GameDictionary(s.charAt(i));
                    }
                    thisNode = thisNode.nextLetters[s.charAt(i) - 'a'];
                }
                // end of branch marked as word
                if(thisNode != this){
                    lastNode = thisNode;
                    thisNode.isWord = true;
                }
            }
        }catch(IOException e){
            return;
        }
    }
    
    // returns true if purely alphabetical word, false otherwise
    // pre: word is already lower case
    public static boolean isAlpha(String word){
        for(int i = 0; i < word.length(); i++){
            if(word.charAt(i) < 'a' || word.charAt(i) > 'z')
                return false;
        }
        return true;
    }
    
    // checks to see if a branch corresponding to prefix exists
    public boolean isPrefix(String prefix){
        if(prefix.length() == 0)
            return true;
        prefix = prefix.toLowerCase();
        GameDictionary thisNode = this;
        for(int i = 0; i < prefix.length(); i++){
            if(prefix.charAt(i) < 'a' || prefix.charAt(i) > 'z')
                return false;
            thisNode = thisNode.nextLetters[prefix.charAt(i) - 'a'];
            // node does not exist
            if(thisNode == null)
                return false;
        }
        return true;
    }
    
    // checks to see if a branch corresponding to word exists
    // and if the end of the branch is marked as a word
    public boolean contains(String word){
        word = word.toLowerCase();
        GameDictionary thisNode = this;
        for(int i = 0; i < word.length(); i++){
            if(word.charAt(i) < 'a' || word.charAt(i) > 'z')
                return false;
            thisNode = thisNode.nextLetters[word.charAt(i) - 'a'];
            if(thisNode == null)
                return false;
        }
        return thisNode.isWord;
    }
   
    public Iterator<String> iterator(){
       return new DictIterator(); 
    }
   
    // Iterator class
    // This class iterates through the Trie by keeping track of the current
    // and moving on to the next word-node
    private class DictIterator implements Iterator<String>{
        ArrayList<GameDictionary> stack = new ArrayList<GameDictionary>();
        GameDictionary thisNode;
        StringBuilder word = new StringBuilder(); // current word
       
        public DictIterator(){
            thisNode = GameDictionary.this;
        }
       
        // if this node is the last node, there is no next node
        public boolean hasNext(){
            return thisNode != lastNode;
        }
        
        // Iteration through a trie is easy with recursion, but next() must
        // return for each word. To implement this we used a stack to simulate
        // recusion among multiple invocations of next
        public String next() throws NoSuchElementException{
            // store previous visited child. char value in each node
            // is how we know the index of the child just visited,
            // allowing us to visit the next child
            char prevChild = 0;
            while(true){
                char i = prevChild;
                while(i < 26 && thisNode.nextLetters[i] == null)
                    i++;
                if(i >= 26){
                    if(stack.size() <= 0)
                        throw new NoSuchElementException();
                    prevChild = (char)(thisNode.letter - 'a' + 1);
                    thisNode = stack.remove(stack.size() - 1);
                    word.deleteCharAt(word.length() - 1);
                }else{ // move down the tree
                    stack.add(thisNode);
                    thisNode = thisNode.nextLetters[i];
                    word.append(thisNode.letter);
                    prevChild = 0;
                    if(thisNode.isWord){
                        return word.toString();
                    }
                }
            }
        }
        // implementation not required
        public void remove() throws UnsupportedOperationException{
            throw new UnsupportedOperationException();
        }
    }
}
