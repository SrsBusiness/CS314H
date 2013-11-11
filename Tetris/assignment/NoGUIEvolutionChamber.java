package assignment;

import java.util.*;
import java.io.*;

public class NoGUIEvolutionChamber{
    protected Board board;
    protected Piece[] pieces;
    protected Piece currentPiece;
    protected int individuals = 50;
    protected NaiveBrain[] brains = new NaiveBrain[individuals];
    protected double[][] champs = new double[5][NaiveBrain.WEIGHTS];
    protected int[] highScores = new int[5];
    protected int generations = 1000;
    protected int trials = 10;
    protected Random random = new Random();
    public NoGUIEvolutionChamber(){
        pieces = TetrisPiece.getPieces();
        for(int i = 0; i < individuals; i++){
            brains[i] = new NaiveBrain();
        }
        readWeights();
    }
    public static void main(String[] args) throws IOException{
        NoGUIEvolutionChamber evo = new NoGUIEvolutionChamber();
        evo.evolve();
    }
    public void readWeights(){
        try(Scanner sc = new Scanner(new File("weights.txt"))){
            for(int i = 0; i < champs.length; i++){
                double[] weights = new double[NaiveBrain.WEIGHTS];
                for(int j = 0; j < NaiveBrain.WEIGHTS; j++){
                    weights[j] = sc.nextDouble();
                }
                champs[i] = weights;
            }
        }catch(IOException e){
        }
    }
    public void breed(){
        for(int i = 0; i < individuals; i++){
            double[] mother = champs[(int)(5 * Math.random())];
            double[] father = champs[(int)(5 * Math.random())];
            double[] child= new double[NaiveBrain.WEIGHTS];
            for(int j = 0; j < NaiveBrain.WEIGHTS; j++){
                child[j] = (mother[j] + father[j]) / 2 + 
                    (Math.random() * 2 - 1) * 50;
            }
            brains[i].setWeights(child);
        }
    }
    public double avgHighScores(){
        double avg = 0;
        for(int i = 0; i < highScores.length; i++){
            avg = (avg * i + highScores[i]) / (i + 1);
        }
        return avg;
    }
    public void evolve() throws IOException{
        for(int i = 0; i < generations; i++){ // each generatation
            long startTime = System.nanoTime();
            long seed = new Random().nextLong();
            for(int j = 0; j < 5; j++){
                highScores[j] = 0;
            }
            breed();
            for(int j = 0; j < individuals; j++){ // each individual
                int lowestScore = Integer.MAX_VALUE;
                for(int k = 0; k < trials; k++){ // each trial
                    random = new Random(seed + k);    
                    int thisScore = playGame(brains[j]);
                    if(thisScore < lowestScore){
                        lowestScore = thisScore;
                    }
                }
                int lowest = minScore(highScores);
                if(lowestScore > highScores[lowest]){
                    highScores[lowest] = lowestScore;
                    champs[lowest] = brains[j].getWeights();
                }
            }
            FileWriter f = new FileWriter(new File("weights.txt"));
            for(int j = 0; j < champs.length; j++){
                for(int k = 0; k < champs[0].length; k++){
                    f.write(String.valueOf(champs[j][k]) + "\n");
                }
            }
            f.flush();
            f.close();
            System.out.printf("Generation %d finished. Time: %f seconds\n" +
                    "Top 5 Average: %f\n", i + 1, (System.nanoTime() - 
                        startTime) / 1000000000.0, avgHighScores());
        }
    }
    private int minScore(int[] scores){
        int lowest = Integer.MAX_VALUE;
        int index = 0;
        for(int i = 0; i < scores.length; i++){
            if(scores[i] < lowest){
                lowest = scores[i];
                index = i;
            }
        }
        return index;
    }
    public int playGame(Brain brain){
        int count = 0;
        Brain.Move move = null;
        board = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT);
        while((move = brain.bestMove(board, currentPiece = pickNextPiece(), 
                        JTetris.HEIGHT, move)) != null) {
            count++;
            if(board.place(move.piece, move.x, move.y) == 
                    TetrisBoard.PLACE_ROW_FILLED)
                board.clearRows();
            board.commit();
        }
        return count;
    }

    public Piece pickNextPiece() {
        int pieceNum;
        pieceNum = (int) (pieces.length * random.nextDouble());
        Piece piece  = pieces[pieceNum];
        return(piece);
    }
}
