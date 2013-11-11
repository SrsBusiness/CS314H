package assignment;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.event.*;

public class JAdversaryTetris extends JTetris {
    private Brain brain;
    public JSlider difficulty;
    public JLabel statusLabel;

    // loads the brain for adversary
    JAdversaryTetris(int width, int height) {
        super(width,height);
        brain = new LameBrain();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris 2000");
        JComponent container = (JComponent)frame.getContentPane();
        container.setLayout(new BorderLayout());

        // Set the metal look and feel
        try {
            UIManager.
                setLookAndFeel(
                        UIManager.
                        getCrossPlatformLookAndFeelClassName() );
        }
        catch (Exception ignored) {}

        // Could create a JTetris or JBrainTetris here
        final int pixels = 16;
        JAdversaryTetris tetris = new JAdversaryTetris(WIDTH*pixels+2, 
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

    // same as JTetris, but adds in adversary slider and status label
    public java.awt.Container createControlPanel() {

        java.awt.Container panel = super.createControlPanel();
        JPanel row2 = new JPanel();
        // Difficulty slider
        panel.add(Box.createVerticalStrut(12));
        row2.add(new JLabel("Adversary:"));
        difficulty = new JSlider(0, 100, 0);	// min, max, current
        difficulty.setPreferredSize(new Dimension(100,15));
        row2.add(difficulty);

        panel.add(row2);
        difficulty.addChangeListener( new ChangeListener() {
            // when the slider changes, sync the timer to its value
            public void stateChanged(ChangeEvent e) {
            }
        });

        statusLabel = new JLabel("ok");
        panel.add(statusLabel);

        return(panel);
    }

    // decides if the adversary chooses piece
    // if so, picks the worst piece of 7
    // otherwise chooses at random
    public Piece pickNextPiece() {
        Random r = new Random();
	Piece piece = pieces[0];
	if(r.nextInt(100)<difficulty.getValue()){    
        board.commit();
        statusLabel.setText("*ok*");     
        Brain.Move move=new Brain.Move();
	    double score=0;
            for(int i=0; i<pieces.length;i++) {
                move = brain.bestMove(board,pieces[i],HEIGHT,move);
                if(move.score>score) {
                    piece=move.piece;
                    score = move.score;
                }
            }
            board.undo();
        }
        else {
            statusLabel.setText("ok");
            int pieceNum;
            pieceNum = (int) (pieces.length * random.nextDouble());
            piece  = pieces[pieceNum];
        }
        return(piece);
    }
}
