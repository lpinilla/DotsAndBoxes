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

    private final float TOTALTIME;

    public IA(Board b, Mode m, int maxDepth, float totalTime, int color, int otherPlayerColor){
        this.b = b;
        this.activeMode = m;
        this.color = color;
        this.otherPlayerColor = otherPlayerColor;
        this.maxDepth = maxDepth;
        this.TOTALTIME = totalTime;
    }

    public Board miniMax(){
        Board sol;
        if(activeMode == Mode.DEPTH){
            sol = depthMinimax();
            b = new Board(b.size, sol.getCurrPlay());
        }else{
            tMinimax();
        }
        return b;
    }


    //Evaluates the board
    //first approach to heuristic
    private int evaluate(Board b){
        return b.numberOfCapturableBoxes(); //vieja heurística
    }

    private int evaluate2(Board b){ //TODO: hacer private
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
        Solution bestMove = null;
        Solution aux;
        aux = dMinimax(b, 0, maxDepth, true);
        if(bestMove == null || bestMove.score < aux.score){
            bestMove = aux;
        }
        return bestMove.move;
    }

    private Solution dMinimax(Board b, int currDepth, int maxDepth, boolean isMax){
        if(currDepth == maxDepth){
            //return evaluate(b);
            return new Solution(b, evaluate2(b));
        }
        int bestVal;
        Solution bestSol = null, aux;
        if(isMax) {
            bestVal = Integer.MIN_VALUE;
            for (Board nBoard : b.getPossibleMoves(b, this.color)) { //ver como manejar si no hay más lugar
                aux = dMinimax(nBoard, currDepth + 1, maxDepth, false);
                if(bestSol == null || bestVal < aux.score){
                    bestVal = aux.score;
                    bestSol = aux;
                }
            }
            return bestSol;
        }else {
            bestVal = Integer.MAX_VALUE;
            bestSol = null;
            aux = null;
            for (Board nBoard : b.getPossibleMoves(b, otherPlayerColor)) {
                aux = dMinimax(nBoard, currDepth + 1, maxDepth, true);
                if (bestSol == null || bestVal > aux.score) {
                    bestVal = aux.score;
                    bestSol = aux;
                }
            }
        }
        return bestSol;
    }

    //Tiene que hacer BFS agarrando la mejor solución que hay por nivel mientras haya tiempo
    public int tMinimax(){
        return 0;
    }
}
