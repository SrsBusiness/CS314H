package assignment;

import java.io.*;
import java.util.Scanner;
public class BetterBrain extends LameBrain{
    public static final int WEIGHTS = 5;
    double[] weights = new double[WEIGHTS];
    public BetterBrain() {
        weights = new double[]{123,14,4,-14,-1.8};
    } 
    // allows the evolver to set the weights on metrics
    public void setWeights(double[] weights){
        this.weights = weights;
    }
    // returns the current metric weights
    public double[] getWeights(){
        return weights;
    }

    // checks all of the metrics on the board
    // returns a low score for a good board, high for a bad
    // rates based on metrics times the weights
    public double rateBoard(Board board){
        int width = board.getWidth();
        int height = board.getHeight();
        
        int holes = 0;
        int blocks = 0;
        int range = 0;
        double avgHeight = 0;
        double avgSquareHeight = 0;
        double varHeight = 0;
        int pits = 0;

        for(int i = 0; i < width; i++){
            avgHeight = (avgHeight * i + board.getColumnHeight(i)) / (i + 1);
            avgSquareHeight = (avgSquareHeight * i + 
                    Math.pow(board.getColumnHeight(i), 2)) / (i + 1);
            range = Math.max(range, board.getMaxHeight() - 
                    board.getColumnHeight(i));
            if(i > 0 && i < width - 1){
                pits += ((board.getColumnHeight(i - 1) + 
                            board.getColumnHeight(i + 1)) / 2 - 
                        board.getColumnHeight(i));
            }
        }
        varHeight = avgSquareHeight - Math.pow(avgHeight, 2);
        
        // holes 
        for(int i = 0; i < board.getWidth(); i++){
            for(int j = 0; j < 4; j++){
                if(!board.getGrid(i, board.getColumnHeight(i) - j - 1)){
                    holes++;
                }
            }
        }
        return (avgHeight * weights[0] + varHeight * weights[1] + holes * 
                weights[2] + range * weights[3] + pits * weights[4]);
    }
}
