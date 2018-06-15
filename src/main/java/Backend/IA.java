package Backend;

public class IA {

    public enum Mode { DEPTH, TIME}
    private Board b;
    private Mode activeMode;
    private int maxDepth, color;

    public IA(Board b, Mode m, int maxDepth, int color){
        this.b = b;
        this.activeMode = m;
        this.color = color;
        if(m == Mode.DEPTH){
            this.maxDepth = maxDepth;
        }
    }

    public void miniMax(){
        if(activeMode == Mode.DEPTH){
            b = depthMinimax(); //no creo que funcione así tan directo
        }else{
            tMinimax();
        }
    }


    //Evaluates the board
    //first approach to heuristic
    private int evaluate(Board b){
        return b.numberOfCapturableBoxes();
    }

    public Board depthMinimax() {
        Board bestMove = null;
        int bestVal = Integer.MIN_VALUE, aux;
        for (Board move : b.getPossibleMoves(b, this.color)) {
            aux = dMinimax(move, 0, maxDepth, true);
            if (aux > bestVal) {
                bestVal = aux;
                bestMove = move;
            }
        }
        return bestMove;
    }

    private int dMinimax(Board b, int currDepth, int maxDepth, boolean isMax){
        if(currDepth == maxDepth){
            return evaluate(b);
        }
        int bestVal, aux;
        if(isMax) {
            bestVal = Integer.MIN_VALUE;
            for (Board nBoard : b.getPossibleMoves(b, this.color)) {
                aux = dMinimax(nBoard, currDepth + 1, maxDepth, false);
                if(bestVal < aux){
                    bestVal = aux;
                }
            }
            return bestVal;
        }
        bestVal = Integer.MAX_VALUE;
        for(Board nBoard : b.getPossibleMoves(b, this.color)) {
            aux = dMinimax(nBoard, currDepth + 1, maxDepth, true);
            if(bestVal > aux){
                bestVal = aux;
            }
        }
        return bestVal;
    }

    //Tiene que hacer BFS agarrando la mejor solución que hay por nivel
    public int tMinimax(){
        return 0;
    }
}
