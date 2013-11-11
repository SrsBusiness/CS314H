/*
    This class is a duplicate of JPieceTest except that it will print 12
    rotations of a piece, regardless of whether it finishes a complete cycle
    of all it's rotations. This is mainly to verify that getPieces() properly
    creates circularly linked Tetris pieces.
*/
package assignment;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.event.*;


/**
  Debugging client for the Piece class.
  The PieceTester component draws all the rotations of a tetris piece.
  PieceTester.main()  creates a frame  with one PieceTester for each
  of the 7 standard tetris pieces.
 */
class PieceTester extends JComponent {
    protected Piece root;
    protected Font font;
    private int w, h;

    public PieceTester(Piece piece, int width, int height) {
        super();

        setPreferredSize(new Dimension(width, height));

        root = piece;
        font = new Font("Monospaced", Font.BOLD, 12);
    }

    /**
      Draws the rotations from left to right.
      Each piece goes in its own little box.
     */
    public final int MAX_ROTATIONS = 12;
    public void paintComponent(Graphics g) {
        // your code here
        Dimension preferredSize = getPreferredSize();
        w = preferredSize.width;
        h = preferredSize.height;
        Piece thisPiece = root;
        for(int i = 0; i < 12; i++){
            drawPiece(g,thisPiece,new Rectangle(i * w / 12, 0, w / 12, h));
            thisPiece = thisPiece.nextRotation();
        }
    }

    /**
      Draw     the piece inside the given rectangle.
     */
    private void drawPiece(Graphics g, Piece piece, Rectangle r) {
        // your code here
        int s = Math.min(w / 12, h);
        int[] skirt = piece.getSkirt();
        for(Point p : piece.getBody()){
            if(p.y == skirt[p.x])
                g.setColor(Color.YELLOW);
            else
                g.setColor(Color.BLACK);
            g.fillRect(p.x * s / 4 + 1 + r.x, s - s * (p.y + 1) / 4 + 1, 
                    s / 4 - 2, s / 4 - 2);
        }
        g.setColor(Color.RED);
        g.drawString("w: " + piece.getWidth() + " h: " +piece.getHeight(),r.x,s);
    }


    /**
      Draws all the pieces by creating a PieceTester for
      each piece, and putting them all in a frame.
     */
    static public void main(String[] args)

    {
        JFrame frame = new JFrame("Piece Tester");
        JComponent container = (JComponent)frame.getContentPane();

        // Put in a BoxLayout to make a vertical list
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        Piece[] pieces = TetrisPiece.getPieces();

        for (int i=0; i<pieces.length; i++) {
            PieceTester test = new PieceTester(pieces[i], 1125, 75);
            container.add(test);
        }

        // Size the window and show it on screen
        frame.pack();
        frame.setVisible(true);

        // Quit on window close
        frame.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                }
                );
    }
}


