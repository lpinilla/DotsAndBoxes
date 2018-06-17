package Backend;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

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
        return  dMinimax(b, 0, maxDepth, true, Integer.MIN_VALUE,
                Integer.MAX_VALUE, new HashMap<Board, Integer>()).move;
    }

    //ver si hay que modificarlo para que sirva también para timeMinimax
    private Solution dMinimax(Board b, int currDepth, int maxDepth, boolean isMax,
                              int alpha, int beta, Map<Board,Integer> map){
        if(currDepth == maxDepth){
            int score = evaluate2(b);
            map.put(b, score);
            return new Solution(b, score);
        }
        int bestVal;
        Solution bestSol = null, aux;
        if(isMax) {
            bestVal = Integer.MIN_VALUE;
            for (Board nBoard : b.getPossibleMoves(b, this.color)) {
                if(!map.containsKey(nBoard)) {
                    aux = dMinimax(nBoard, currDepth + 1, maxDepth, false, alpha, beta, map);
                    map.put(nBoard, aux.score);
                }else{
                    aux = new Solution(nBoard, map.get(nBoard));
                }
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
                if(!map.containsKey(nBoard)) {
                    aux = dMinimax(nBoard, currDepth + 1, maxDepth, true, alpha, beta, map);
                    map.put(nBoard, aux.score);
                }else{
                    aux = new Solution(nBoard, map.get(nBoard));
                }
                if (aux != null && (bestSol == null || bestVal > aux.score)) {
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
        Map<Board, Integer> map = new HashMap<>();
        do{
            for(int depth = 0; hasTime(maxTime) && depth < MAXDEPTH; depth++){
                if(hasTime(maxTime)) {
                    aux = dMinimax(b, 0, depth, true, Integer.MIN_VALUE, Integer.MAX_VALUE, map);
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
        return System.currentTimeMillis() <= maxTime;
    }


    /*
    public void saveGame(String fileName){
        try {
            PrintWriter writer = new PrintWriter("src/test/java/" +fileName + ".txt", "UTF-8");
            writer.println(size);
            writer.println(currPlay);
            StringBuffer fila;
            for (int i = 0; i < size; i++) {
                fila = new StringBuffer();
                for (int j = 0; j < size; j++) {
                    fila.append(boxConfiguration(matrix[i][j]));
                    fila.append("-");
                    fila.append(matrix[i][j].color);
                    fila.append(" ");
                }
                writer.println(fila);
            }
            writer.close();
        }catch (IOException e){
            e.getMessage(); //hacer algo con la exception. (mostrarla en pantalla)?
        }
    }*/

    public void saveDOTFile(){
        try{
            PrintWriter writer = new PrintWriter("src/main/java/SavedGames/TreeSave.dot", "UTF-8");
            //writer.pritnln a partir de acá
            /*
            En cada pasada: Indicar b->nMove
            nMove [estilos]
            en selección de best
            nMove [estilos2]
            en poda
            nMode[estilo3]
             */
            writer.close();
        }catch(IOException e){
            e.getMessage(); //tal vez hacer algo con la exception;
        }
    }
}
