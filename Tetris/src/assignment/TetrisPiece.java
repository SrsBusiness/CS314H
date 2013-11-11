package assignment;

import java.awt.*;
import java.util.*;

/**
   An immutable representation of a tetris piece in a particular rotation.
   Each piece is defined by the blocks that make up its body.

   This is the starter file version -- a few simple things are filled
   in already.

   @author	Nick Parlante
   @version	1.0, Mar 1, 2001
*/
public final class TetrisPiece extends Piece {
    private static Piece[] pieces;
    private Point[] body;
    private int[] skirt;
    private int width;
    private int height;

    /**
       Defines a new piece given the Points that make up its body.
       Makes its own copy of the array and the Point inside it.
       Does not set up the rotations.

       This constructor is PRIVATE -- if a client
       wants a piece object, they must use Piece.getPieces().
    */
    private TetrisPiece(Point[] points) {
	// your code here
        body = points;
    }

    /**
       Returns the width of the piece measured in blocks.
    */

    // if this is the first time getWidth or getHeight is called,
    // it will calculate it. Otherwise, it will simply return it.
    // these methods assume neither the width or height is ever 0
    public int getWidth() {
	// your code here
	if(width != 0)
            return width;
        int x = 0;
        for(int i = 0; i < body.length; i++){
            if(body[i].x > x)
                x = body[i].x;
        }
        return width = x + 1;
    }

    /**
       Returns the height of the piece measured in blocks.
    */
    public int getHeight() {
	// your code here
	if(height != 0)
            return height;
        int y = 0;
        for(int i = 0; i < body.length; i++){
            if(body[i].y > y)
                y = body[i].y;
        }
        return height = y + 1;
    }

    /**
       Returns a pointer to the piece's body. The caller
       should not modify this array.
    */
    public Point[] getBody() {
	return body;
    }

    /**
       Returns a pointer to the piece's skirt. For each x value
       across the piece, the skirt gives the lowest y value in the body.
       This useful for computing where the piece will land.
       The caller should not modify this array.
    */

    // Method filters through all the points for the lowest y value for each x
    // Will calculate it the first time, and simply return skirt in subsequent
    // calls
    public int[] getSkirt() {
	if(skirt != null){
            return skirt;
        }
        // width of skirt is the width of the piece
        skirt = new int[getWidth()];
        for(int i = 0; i < skirt.length; i++){
            skirt[i] = Integer.MAX_VALUE;
        }
        for(int i = 0; i < body.length; i++){
            if(body[i].y < skirt[body[i].x])
                skirt[body[i].x] = body[i].y;
        }
        return skirt;
    }

    /**
       Returns a piece that is 90 degrees counter-clockwise
       rotated from the receiver.

       <p>Implementation:
       The Piece class pre-computes all the rotations once.
       This method just hops from one pre-computed rotation
       to the next in constant time.
    */
    public Piece nextRotation() { // why is this method here??
	//return super.nextRotation();
        return next;
    }

    /**
       Returns true if two pieces are the same --
       their bodies contain the same points.
       Interestingly, this is not the same as having exactly the
       same body arrays, since the points may not be
       in the same order in the bodies. Used internally to detect
       if two rotations are effectively the same.
    */

    public boolean equals(Piece other) {
        Point[] otherBody = other.getBody();
	for(int i = 0; i < body.length; i++){
            boolean found = false;
            for(int j = 0; j < otherBody.length; j++){
                if(body[i].equals(otherBody[j])){
                    found = true;
                    break;
                }
            }
            if(!found){
                return false;
            }
        }
        return true;
    }

    /**
       Returns an array containing the first rotation of
       each of the 7 standard tetris pieces.
       The next (counterclockwise) rotation can be obtained
       from each piece with the {@link #nextRotation()} message.
       In this way, the client can iterate through all the rotations
       until eventually getting back to the first rotation.
    */
    public static Piece[] getPieces() {
        if(pieces != null)
            return pieces;
        pieces = new Piece[pieceStrings.length];
        for(int i = 0; i < pieces.length; i++){
            pieces[i] = new TetrisPiece(parsePoints(pieceStrings[i]));
            // piece we are currently interested in
            Piece thisPiece = pieces[i];
            Piece rotate;
            while(!(rotate = rotate(thisPiece)).equals(pieces[i])){
                thisPiece.next = rotate;
                thisPiece = thisPiece.next;
            }
            thisPiece.next = pieces[i];
        }
        return pieces;
    }
    // Method calculates the next anticlockwise rotation of a given piece
    // and returns a new piece object with the rotated body
    private static Piece rotate(Piece p){
        Point[] rBody = new Point[p.getBody().length];
        for(int i = 0; i < p.getBody().length; i++){
            // to rotate, swap x and y values, then reflect about the central
            // vertical axis of the piece
            rBody[i] = new Point(p.getHeight() - p.getBody()[i].y - 1, p.getBody()[i].x);
        }
        return new TetrisPiece(rBody);
    }
}
