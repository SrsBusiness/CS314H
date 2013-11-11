package assignment;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.event.*;

public class JBrainTetris extends JTetris {
    protected Brain brain;
    protected int oldCount = -1;
    protected boolean brainActive = true;
    protected Brain.Move targetMove;

    JBrainTetris(int width, int height) {
	super(width, height);
	brain = new LameBrain();
    }


    public void tick(int verb) {
	if (!gameOn) return;

	if (currentPiece != null) {
	    board.undo();	// remove the piece from its old position
	}

	// if the brain is playing, get and do the next best move
	if (verb == DOWN && brainActive) {
	    if (count != oldCount) {
		targetMove = brain.bestMove(board, currentPiece, 
					    board.getHeight(), null);
		oldCount = count;
	    }
	    if (targetMove != null) {
		if (targetMove.x < currentX)
		    super.tick(LEFT);
		else if (targetMove.x > currentX)
		    super.tick(RIGHT);
		if (!currentPiece.equals(targetMove.piece))
		    super.tick(ROTATE);
	    }
	}
	super.tick(verb);
    }

    public void stopGame() {
	super.stopGame();
	oldCount = -1;
    }
    public static void main(String[] args)

    {
	JFrame frame = new JFrame("Tetris 2004");
	JComponent container = (JComponent)frame.getContentPane();
	container.setLayout(new BorderLayout());

	// Set the metal look and feel
	try {
	    UIManager.
		setLookAndFeel(UIManager.
			           getCrossPlatformLookAndFeelClassName() );
	}
	catch (Exception ignored) {
	    // suppress exceptions, probably a bad idea
	}

	// You can create a JTetris or JBrainTetris here
	final int pixels = 16;
	JTetris tetris = new JBrainTetris(WIDTH*pixels+2, 
					  (HEIGHT+TOP_SPACE)*pixels+2);


	container.add(tetris, BorderLayout.CENTER);


	if (args.length != 0 && args[0].equals("test")) {
	    tetris.testMode = true;
	}

	Container panel = tetris.createControlPanel();

	// Add the quit button last so it's at the bottom
	panel.add(Box.createVerticalStrut(12));
	JButton quit = new JButton("Quit");
	panel.add(quit);
	quit.addActionListener( new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    System.exit(0);
		}
	    });


	container.add(panel, BorderLayout.EAST);
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
