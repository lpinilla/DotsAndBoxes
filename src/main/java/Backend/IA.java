package Backend;

public class IA {

    public enum Mode { DEPTH, TIME}

    private class Solution{
        Board move;
        int score;

        Solution(Board b, int score){
            this.move = b;
            this.score = score;
        }
    }

    private Board b;
    private Mode activeMode;
    private int maxDepth, color, otherPlayerColor;
    private boolean prune;

    private final long TOTALTIME;

    public IA(Board b, Mode m, int maxDepth, long totalTime, int color, int otherPlayerColor, boolean prune){
        this.b = b;
        this.activeMode = m;
        this.color = color;
        this.otherPlayerColor = otherPlayerColor;
        this.maxDepth = maxDepth;
        this.TOTALTIME = totalTime;
        this.prune = prune;
    }

    public Board miniMax(){
        Board sol;
        if(activeMode == Mode.DEPTH){
            sol = depthMinimax();
            b = new Board(b.size, sol.getCurrPlay());
        }else{
            b = timeMinimax();
        }
        return b;
    }


    //Evaluates the board
    //first approach to heuristic
    private int evaluate(Board b){
        return b.numberOfCapturableBoxes(); //vieja heurística
    }

    private int evaluate2(Board b){
        return b.differenceInBoxesOfColor(color, otherPlayerColor);
    }

    /*public Board depthMinimax() {
        Board bestMove = null;
        int bestVal = Integer.MIN_VALUE;
        Solution aux;
        for (Board move : b.getPossibleMoves(b, color)) {
            aux = dMinimax(move, 0, maxDepth, true);
            if (aux.score > bestVal) {
                bestVal = aux;
                bestMove = move;
            }
        }
        return bestMove;
    }*/

    public Board depthMinimax(){
        return  dMinimax(b, 0, maxDepth, true, Integer.MIN_VALUE, Integer.MAX_VALUE).move;
    }

    private Solution dMinimax(Board b, int currDepth, int maxDepth, boolean isMax, int alpha, int beta){
        if(currDepth == maxDepth){
            return new Solution(b, evaluate2(b));
        }
        int bestVal;
        Solution bestSol = null, aux;
        if(isMax) {
            bestVal = Integer.MIN_VALUE;
            for (Board nBoard : b.getPossibleMoves(b, this.color)) { //ver como manejar si no hay más lugar
                aux = dMinimax(nBoard, currDepth + 1, maxDepth, false, alpha, beta);
                if(aux != null &&  (bestSol == null || bestVal < aux.score)){
                    bestVal = aux.score;
                    bestSol = aux;
                }
                alpha = Math.max(alpha, bestVal);
                if(this.prune && beta <= alpha){
                    break;
                }
            }
            return bestSol;
        }else {
            bestVal = Integer.MAX_VALUE;
            bestSol = null;
            aux = null;
            for (Board nBoard : b.getPossibleMoves(b, otherPlayerColor)) {
                aux = dMinimax(nBoard, currDepth + 1, maxDepth, true, alpha, beta);
                if (bestSol == null || (aux != null && bestVal > aux.score)) {
                    bestVal = aux.score;
                    bestSol = aux;
                }
                beta = Math.min(beta, bestVal);
                if(prune && beta <= alpha){
                    break;
                }
            }
        }
        return bestSol;
    }

    //Iterative deepening
    public Board timeMinimax(){
        int MAXDEPTH = 2;
        Solution bestSol = null, aux = null;
        final long maxTime = System.currentTimeMillis() + TOTALTIME;
        long currtime = System.currentTimeMillis();
        do{
            for(int depth = 0; hasTime(maxTime) && depth < MAXDEPTH; depth++){
                if(hasTime(maxTime)) {
                    aux = dMinimax(b, 0, depth, true, Integer.MIN_VALUE, Integer.MAX_VALUE);
                }
                if(bestSol == null || (aux != null && aux.score > bestSol.score)){
                    bestSol = aux;
                }
            }
            MAXDEPTH++;
        }while(hasTime(maxTime));
        if(bestSol == null){
            throw new RuntimeException("Not enough time"); //cambiar después el tipo de exception
        }
        return bestSol.move;
    }

    private boolean hasTime(long maxTime){
        long time = System.currentTimeMillis();
        return System.currentTimeMillis() <= maxTime;
    }
}
