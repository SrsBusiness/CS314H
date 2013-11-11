package assignment;

import java.awt.*;
import java.util.*;


/**
   Represents a Tetris board -- essentially a 2-d grid of
   booleans. Supports tetris pieces and row clearning.  Has an "undo"
   feature that allows clients to add and remove pieces efficiently.
   Does not do any drawing or have any idea of pixels. Intead, just
   represents the abtsract 2-d board.

   This is the starter file version -- a few simple things are filled
   in already

   @author	Nick Parlante   w/changes by Walter Chang
   @version	1.0.1, Sep 19, 2004
*/
public class TetrisBoard implements Board {

    private boolean DEBUG = true;
    private boolean[][] board;
    private boolean[][] boardBackup;
    private int maxHeight;
    private int xmaxHeight;
    private int[] widths;
    private int[] heights;
    private int[] xWidths;
    private int[] xHeights;
    boolean committed = true;

    /**
       Creates an empty board of the given width and height
       measured in blocks.
    */
    public TetrisBoard(int Width, int Height) {
        board = new boolean[Height][Width];
        boardBackup = new boolean[Height][Width];
        widths = new int[Height];
        xWidths = new int[Height];
        heights = new int[Width];
        xHeights = new int[Width];
    }


    /**
       Returns the width of the board in blocks.
    */
    public int getWidth() {
        return board[0].length;
    }


    /**
       Returns the height of the board in blocks.
    */
    public int getHeight() {
        return board.length;
    }


    /**
       Returns the max column height present in the board.
       For an empty board this is 0.
    */
    public int getMaxHeight() {
        // can either iterate each time. bad. or keep an updated array of heights/widths.
        return maxHeight;
    }


    /**
       Checks the board for internal consistency -- used
       for debugging.
    */
    public void sanityCheck() {
        if (DEBUG) {
	    // consistency check the board state

            // heights
            for(int i = 0; i < heights.length; i++){
                int boardHeight = board.length;
                for(int j = board.length - 1; j >= 0 && !board[j][i]; j--){
                    boardHeight = j;
                }
                if(boardHeight != heights[i])
                    System.out.printf("Heights[%d] is %d but should be %d\n", i, 
                            heights[i], boardHeight);
            }
            // widths
            for(int i = 0; i < widths.length; i++){
                int boardWidth = 0;
                for(int k = board.length-1; k > i; k--) {
                    if(board[i]==board[k])
                    System.out.printf("board[%d] is the same pointer as board[%d]\n",i,k);
                }
                for(int j = board[0].length - 1; j >= 0; j--){
                    if(board[i][j])
                        boardWidth++;
                }
                if(boardWidth != widths[i])
                    System.out.printf("Widths[%d] is %d but should be %d\n", i, 
                            widths[i], boardWidth);
            }
            // xHeights

            // xWidths
            

        }
    }

    /**
       Given a piece and an x, returns the y
       value where the piece would come to rest
       if it were dropped straight down at that x.

       <p>
       Implementation: use the skirt and the col heights
       to compute this fast -- O(skirt length).
    */
    public int dropHeight(Piece piece, int x) {
        int[] skirt = piece.getSkirt();
        int maxY = 0;
        for(int i = 0; i < skirt.length && i + x < board[0].length; i++) 
            maxY = Math.max(maxY, heights[i + x] - skirt[i]);
        return maxY;
    }


    /**
       Returns the height of the given column --
       i.e. the y value of the highest block + 1.
       The height is 0 if the column contains no blocks.
    */
    public int getColumnHeight(int x) {
        return heights[x];
    }


    /**
       Returns the number of filled blocks in
       the given row.
    */
    public int getRowWidth(int y) {
        return widths[y];
    }


    /**
       Returns true if the given block is filled in the board.
       Blocks outside of the valid width/height area
       always return true.
    */
    public final boolean getGrid(int x, int y) {
        try {
            return board[y][x];
        } catch(ArrayIndexOutOfBoundsException e) {
            return true;
        }
    }


    public static final int PLACE_OK = 0;
    public static final int PLACE_ROW_FILLED = 1;
    public static final int PLACE_OUT_BOUNDS = 2;
    public static final int PLACE_BAD = 3;

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
    public int place(Piece piece, int x, int y) {
        if(!committed) {
            System.err.println("not committed when placing");
            return PLACE_BAD;
        }
        if((piece.getWidth() + x) > board[0].length || 
                (piece.getHeight() + y) > board.length ||
                x < 0 || y < 0){
            return PLACE_OUT_BOUNDS;
        }
        // back up current state
        for(int i = 0; i < board.length; i++)
            System.arraycopy(board[i], 0, boardBackup[i], 0, board[0].length);
        System.arraycopy(widths, 0, xWidths, 0, widths.length);
        System.arraycopy(heights, 0, xHeights, 0, heights.length);
        xmaxHeight = maxHeight;
        
        committed = false;
        Point[] body = piece.getBody();
        int newMaxHeight = maxHeight;
        // place the piece
        for(int i = 0; i < body.length; i++) {
            if(board[body[i].y + y][body[i].x + x]) {
                return PLACE_BAD;
            }
            board[body[i].y + y][body[i].x + x] = true;
            widths[body[i].y + y]++;
            heights[body[i].x + x] = Math.max(heights[body[i].x + x],body[i].y + y + 1);
            newMaxHeight = Math.max(newMaxHeight, heights[body[i].x + x]);
        }
        maxHeight = newMaxHeight;
        // check for filled rows
        for(int i = 0; i < piece.getHeight(); i++){
            if(widths[i + y] == board[0].length)
                return PLACE_ROW_FILLED;
        }
        sanityCheck();
        return PLACE_OK;
    }

    /**
       Deletes rows that are filled all the way across, moving
       things above down. Returns true if any row clearing happened.

       <p>Implementation: This is complicated.
       Ideally, you want to copy each row down
       to its correct location in one pass.
       Note that more than one row may be filled.
    */
    public boolean clearRows() {
	int currentRow = 0;
        int copyRow = 0;
        boolean rowCleared = false;
        committed = false;
        // copies the reference copyRow to currentRow, incrementing copy
        // row for each filled row
        for(; currentRow < maxHeight; currentRow++, copyRow++){
            while(copyRow < board.length && widths[copyRow] == 
                    board[0].length){ 
                copyRow++;
                maxHeight--;
                rowCleared = true;
            }
            if(copyRow >= board.length){
                board[currentRow] = new boolean[board[0].length];
                widths[currentRow] = 0;
            }else{
                board[currentRow] = board[copyRow];
                widths[currentRow] = widths[copyRow];
            }
        }
        // when copyRow is off the board, reset current row to new cleared rows
        for(; currentRow < copyRow && currentRow < board.length; currentRow++) {
            board[currentRow] = new boolean[board[0].length];
            widths[currentRow] = 0;
        }
        // update heights
        for(int i = 0; i < heights.length; i++) {
	    int j;
            for(j = heights[i]; j >= 1 && !board[j - 1][i]; j--){
            }
            heights[i] = j;
        }
        sanityCheck();
        return rowCleared;
    }



    /**
       If a place() happens, optionally followed by a clearRows(),
       a subsequent undo() reverts the board to its state before
       the place(). If the conditions for undo() are not met, such as
       calling undo() twice in a row, then the second undo() does nothing.
       See the overview docs.
    */
    public void undo() {
        if(committed)
            return;

        boolean[][] tempBool = board;
        board = boardBackup;
        boardBackup = tempBool;
        
        int[] tempInt = widths;
        widths = xWidths;
        xWidths = tempInt;
        
        tempInt = heights;
        heights = xHeights;
        xHeights = tempInt;
        
        maxHeight = xmaxHeight;

        committed = true;
        sanityCheck();
    }


    /**
       Puts the board in the committed state.
       See the overview docs.
    */
    public void commit() {
        // backups
        for(int i = 0; i < board.length; i++)
            System.arraycopy(board[i], 0, boardBackup[i], 0, board[0].length);
        System.arraycopy(widths, 0, xWidths, 0, widths.length);
        System.arraycopy(heights, 0, xHeights, 0, heights.length);
        xmaxHeight = maxHeight;

        committed = true;
    }
}
