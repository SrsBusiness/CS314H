package assignment;
public class NaiveBrain extends LameBrain{
    public static final int WEIGHTS = JTetris.WIDTH * JTetris.Height; 
    double[] weights = new double[WEIGHTS];
    public void setWeights(double[] weights){
        if(weights.length == this.weights.length){
        }
    }
    public void getWeights(){
        return weights;
    }
    public double rateBoard(Board board){
        double score = 0;
        for(int i = 0; i < board.getWidth(); i++){
            for(int j = 0; j < board.getHeight(); j++){
                if(board.getGrid(i,j))
                    score += weights[i + j * board.getWidth()]
            }
        }
        return score;
    }
}
