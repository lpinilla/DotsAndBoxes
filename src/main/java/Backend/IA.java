package Backend;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

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

    //.dot objects
    private Queue<String> dotFileBuilder;
    private ByteArrayOutputStream baos;
    private PrintStream ps, original;

    private final long TOTALTIME;

    public IA(Board b, Mode m, int maxDepth, long totalTime, int color, int otherPlayerColor, boolean prune){
        this.b = b;
        this.activeMode = m;
        this.color = color;
        this.otherPlayerColor = otherPlayerColor;
        this.maxDepth = maxDepth;
        this.TOTALTIME = totalTime;
        this.prune = prune;
        dotFileBuilder = new LinkedList<>();
        baos = new ByteArrayOutputStream();
        ps = new PrintStream(baos);
    }

    public Board miniMax(){
        dotFileBuilder.offer("digraph Tree{");
        Board sol;
        if(activeMode == Mode.DEPTH){
            sol = depthMinimax();
            //b = new Board(b.size, sol.getCurrPlay());
        }else{
            sol = timeMinimax();
        }
        this.b = sol;
        dotFileBuilder.offer("}");
        return sol;
    }

    public void swapColors(){
        int aux = color;
        color = otherPlayerColor;
        otherPlayerColor = aux;
    }


    //Evaluates the board

    private int evaluate2(Board b){
        return b.differenceInBoxesOfColor(color, otherPlayerColor);
    }

    public Board depthMinimax(){
        return dMinimax(b, 0, maxDepth, true, Integer.MIN_VALUE,
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
        StringBuffer dotBuilderCurrentBoard = new StringBuffer(), dotBuilderNewBoard = new StringBuffer();
        if(isMax) {
            bestVal = Integer.MIN_VALUE;
            for (Board nBoard : b.getPossibleMoves(b, this.color)) {
                //dot file print
                b.asciiPrintBoard(dotBuilderCurrentBoard);
                nBoard.asciiPrintBoard(dotBuilderNewBoard);
                dotFileBuilder.offer("\"" + dotBuilderCurrentBoard + "\"" + "->" +
                                        "\"" + dotBuilderNewBoard + "\"");

                if(!map.containsKey(nBoard)) {
                    aux = dMinimax(nBoard, currDepth + 1, maxDepth, false, alpha, beta, map);
                    map.put(nBoard, aux.score);
                }else{
                    aux = new Solution(nBoard, map.get(nBoard));
                }
                if(aux != null &&  (bestSol == null || bestVal < aux.score)){
                    dotFileBuilder.offer("\"" + dotBuilderNewBoard + "\"" + "[style=filled,color=green]");
                    bestVal = aux.score;
                    bestSol = new Solution(nBoard, bestVal);
                }
                alpha = Math.max(alpha, bestVal);
                if(this.prune && beta <= alpha){
                    dotFileBuilder.offer("\"" + dotBuilderNewBoard + "\"" + "[style=filled,color=red]");
                    break;
                }
                dotBuilderCurrentBoard = new StringBuffer();
                dotBuilderNewBoard = new StringBuffer();
            }
            return bestSol;
        }else {
            bestVal = Integer.MAX_VALUE;
            bestSol = null;
            aux = null;
            for (Board nBoard : b.getPossibleMoves(b, otherPlayerColor)) {

                b.asciiPrintBoard(dotBuilderCurrentBoard);
                nBoard.asciiPrintBoard(dotBuilderNewBoard);
                dotFileBuilder.offer("\"" + dotBuilderCurrentBoard + "\"" + "->" +
                        "\"" + dotBuilderNewBoard + "\"");

                if(!map.containsKey(nBoard)) {
                    aux = dMinimax(nBoard, currDepth + 1, maxDepth, true, alpha, beta, map);
                    map.put(nBoard, aux.score);
                }else{
                    aux = new Solution(nBoard, map.get(nBoard));
                }
                if (aux != null && (bestSol == null || bestVal > aux.score)) {
                    dotFileBuilder.offer("\"" + dotBuilderNewBoard + "\"" + "[style=filled,color=blue]");
                    bestVal = aux.score;
                    bestSol = aux;
                }
                beta = Math.min(beta, bestVal);
                if(prune && beta <= alpha){
                    dotFileBuilder.offer("\"" + dotBuilderNewBoard + "\"" + "[style=filled,color=red]");
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

    public void saveDOTFile(){
        try{
            PrintWriter writer = new PrintWriter("src/main/java/SavedGames/TreeSave.dot", "UTF-8");
            while(!dotFileBuilder.isEmpty()){
                writer.println(dotFileBuilder.poll());
            }
            writer.close();
        }catch(IOException e){
            e.getMessage(); //tal vez hacer algo con la exception;
        }
    }
}
