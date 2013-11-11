package assignment;

import java.awt.*;
import java.util.*;

/**
   An immutable representation of a tetris piece in a particular rotation.
   Each piece is defined by the blocks that make up its body.

   This is the basis for a piece in Tetris.

   @author	Nick Parlante   w/ modifications by Matthew Alden
   @version	1.0, Mar 1, 2001
*/
public abstract class Piece {
    // The "next" rotation of this Piece.  This member forms the basis
    // for the circularly linked list of piece rotations.
    protected Piece next;

    // These Strings represent the seven type of pieces that are used
    // to play Tetris.
    static protected String[] pieceStrings = { "0 0  0 1  0 2  0 3",
					       "0 0  0 1  0 2  1 0", 
					       "0 0  1 0  1 1  1 2", 
					       "0 0  1 0  1 1  2 1",
					       "0 1  1 1  1 0  2 0", 
					       "0 0  0 1  1 0  1 1", 
					       "0 0  1 0  1 1  2 0"};


    /**
       Returns the width of the piece measured in blocks.
    */
    public abstract int getWidth();


    /**
       Returns the height of the piece measured in blocks.
    */
    public abstract int getHeight();


    /**
       Returns a pointer to the piece's body. The caller
       should not modify this array.
    */
    public abstract Point[] getBody();


    /**
       Returns a pointer to the piece's skirt. For each x value
       across the piece, the skirt gives the lowest y value in the body.
       This useful for computing where the piece will land.
       The caller should not modify this array.
    */
    public abstract int[] getSkirt();


    /**
       Returns a piece that is 90 degrees counter-clockwise
       rotated from the receiver.

       Implementation:
       The Piece class pre-computes all the rotations once.
       This method just hops from one pre-computed rotation
       to the next in constant time.
    */
    public Piece nextRotation() {
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
    public abstract boolean equals(Piece other);


    /**
       Given a string of x,y pairs., i.e. ("0 0  0 1  0 2  1 0"), parses
       the points into a Point[] array.
       (Provided code)
    */
    protected static Point[] parsePoints(String string) {
	// could use Arraylist here, but use vector so works on Java 1.1
	Vector points = new Vector();
	StringTokenizer tok = new StringTokenizer(string);
	try {
	    while(tok.hasMoreTokens()) {
		int x = Integer.parseInt(tok.nextToken());
		int y = Integer.parseInt(tok.nextToken());

		points.addElement(new Point(x, y));
	    }
	}
	catch (NumberFormatException e) {
	    throw new RuntimeException("Could not parse x,y string:" + string);
	}

	// Make an array out of the Vector
	Point[] array = new Point[points.size()];
	points.copyInto(array);
	return(array);
    }
}
