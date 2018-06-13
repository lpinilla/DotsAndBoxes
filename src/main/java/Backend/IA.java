package Backend;

public class IA {

    public enum Mode { DEPTH, TIME}
    Board b;
    Mode activeMode;
    int maxDepth;

    public IA(Board b, Mode m, int maxDepth){
        this.b = b;
        this.activeMode = m;
        if(m == Mode.DEPTH){
            this.maxDepth = maxDepth;
        }
    }

    public void miniMax(){
        /*if(activeMode == Mode.DEPTH){
            dMinimax();
        }else{
            tMinimax();
        }*/
    }


    //Evaluates the board
    //first approach to heruristic
    public int evaluate(Board b){
        int boxesICanCapture = b.numberOfCapturableBoxes();
        return boxesICanCapture;
    }

    public int depthMinimax(){
        return dMinimax(b, 0, maxDepth, true);
    }

    private int dMinimax(Board b, int currDepth, int maxDepth, boolean isMax){
        if(currDepth == maxDepth){
            return evaluate(b);
        }
        int bestVal, aux;
        if(isMax) {
            bestVal = Integer.MIN_VALUE;
            for (Board nBoard : b.getPossibleMoves()) {
                aux = dMinimax(b, currDepth + 1, maxDepth, false);
                bestVal = Math.max(bestVal, aux);
            }
            return bestVal;
        }
        bestVal = Integer.MAX_VALUE;
        for(Board nBoard : b.getPossibleMoves()) {
            aux = dMinimax(b, currDepth + 1, maxDepth, true);
            bestVal = Math.min(bestVal, aux);
        }
        return bestVal;
    }
}
