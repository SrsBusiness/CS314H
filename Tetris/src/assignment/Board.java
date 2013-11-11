package assignment;

import java.awt.*;
import java.util.*;

/**
  Implementers of this interface represent a Tetris board --
  essentially a 2-d grid of booleans. Supports tetris pieces and row
  clearning.  Has an "undo" feature that allows clients to add and
  remove pieces efficiently.  Does not do any drawing or have any
  idea of pixels. Intead, just represents the abstract 2-d board.

  @author    Nick Parlante   w/ changes by Matthew Alden, Walter Chang
  @version   1.0.1, Sep 17, 2004
 */
interface Board {
    /**
      Returns the width of the board in blocks.
     */
    int getWidth();


    /**
      Returns the height of the board in blocks.
     */
    int getHeight();


    /**
      Returns the max column height present in the board.
      For an empty board this is 0.
     */
    int getMaxHeight();


    /**
      Checks the board for internal consistency -- used
      for debugging.
     */
    void sanityCheck();


    /**
      Given a piece and an x, returns the y
      value where the piece would come to rest
      if it were dropped straight down at that x.

Implementation: use the skirt and the col heights
to compute this fast -- O(skirt length).
     */
    int dropHeight(Piece piece, int x);


    /**
      Returns the height of the given column --
      i.e. the y value of the highest block + 1.
      The height is 0 if the column contains no blocks.
     */
    int getColumnHeight(int x);


    /**
      Returns the number of filled blocks in
      the given row.
     */
    int getRowWidth(int y);


    /**
      Returns true if the given block is filled in the board.
      Blocks outside of the valid width/height area
      always return true.
     */
    boolean getGrid(int x, int y);


    static final int PLACE_OK = 0;
    static final int PLACE_ROW_FILLED = 1;
    static final int PLACE_OUT_BOUNDS = 2;
    static final int PLACE_BAD = 3;

    /**
      Attempts to add the body of a piece to the board.
      Copies the piece blocks into the board grid.
      Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
      for a regular placement that causes at least one row to be filled.

      Error cases:
      If part of the piece would fall out of bounds, the placement
      does not change the board at all, and PLACE_OUT_BOUNDS is
      returned.  If the placement is "bad" --interfering with
      existing blocks in the grid -- then the placement is halted
      partially complete and PLACE_BAD is returned.  An undo() will
      remove the bad placement.
     */
    int place(Piece piece, int x, int y);


    /**
      Deletes rows that are filled all the way across, moving
      things above down. Returns true if any row clearing happened.

Implementation: This is complicated.
Ideally, you want to copy each row down
to its correct location in one pass.
Note that more than one row may be filled.
     */
    boolean clearRows();


    /**
      If a place() happens, optionally followed by a clearRows(),
      a subsequent undo() reverts the board to its state before
      the place(). If the conditions for undo() are not met, such as
      calling undo() twice in a row, then the second undo() does nothing.
      See the overview docs.
     */
    void undo();


    /**
      Puts the board in the committed state.
      See the overview docs.
     */
    void commit();
}
