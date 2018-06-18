import Backend.Board;
import Backend.GameManager;
import Backend.IA;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IATest {

    private IA jarvis;
    private GameManager gm;

    @Before
    public void before(){
        gm = new GameManager();
        jarvis = new IA(gm.getBoard(), IA.Mode.DEPTH, 1, 0, 1,2, false);
    }

    //Depth tests


    //Analizar si dada una caja que es capturable, la agarra
    @Test
    public void bestBoardInLimitedSpace(){
        Board b = new Board(2);
        jarvis = new IA(b, IA.Mode.DEPTH, 1, 0,1,2, false);
        b.makeMove(b, 0,0, Board.DIRECTIONS.TOP, 1);
        b.makeMove(b, 0,0, Board.DIRECTIONS.RIGHT, 1);
        b.makeMove(b, 0,0, Board.DIRECTIONS.LEFT, 1);
        b.makeMove(b, 0,0, Board.DIRECTIONS.BOTTOM, 1);
        b.makeMove(b, 1,0, Board.DIRECTIONS.RIGHT, 2);
        b.makeMove(b, 1,0, Board.DIRECTIONS.LEFT, 2);
        b.makeMove(b, 1,0, Board.DIRECTIONS.BOTTOM, 2);
        b.makeMove(b, 0,1, Board.DIRECTIONS.TOP, 1);
        b.makeMove(b,0,1, Board.DIRECTIONS.RIGHT, 1);
        b.makeMove(b, 1,1, Board.DIRECTIONS.BOTTOM,2);
        b.asciiPrintBoard();
        Board best = jarvis.depthMinimax();
        best.asciiPrintBoard();
    }

    @Test
    public void completeSquareIA(){
        Board b = new Board(2);
        jarvis = new IA(b, IA.Mode.DEPTH, 1, 0,1,2, false);
        b.makeMove(b, 0,0, Board.DIRECTIONS.TOP, 1);
        b.makeMove(b, 0,0, Board.DIRECTIONS.RIGHT, 1);
        b.makeMove(b, 0,0, Board.DIRECTIONS.LEFT, 1);
        b.asciiPrintBoard();
        Board best = jarvis.depthMinimax();
        best.asciiPrintBoard();
    }

    @Test
    public void completeSquareIABig(){
        Board b = new Board(6);
        jarvis = new IA(b, IA.Mode.DEPTH, 3, 0,1,2,false);
        jarvis = new IA(b, IA.Mode.DEPTH, 1, 0,1,2, false);
        b.makeMove(b, 0,0, Board.DIRECTIONS.TOP, 1);
        b.makeMove(b, 0,0, Board.DIRECTIONS.RIGHT, 1);
        b.makeMove(b, 0,0, Board.DIRECTIONS.LEFT, 1);
        b.asciiPrintBoard();
        Board best = jarvis.depthMinimax();
        best.asciiPrintBoard();
    }

    //Time tests

    @Test
    public void bestBoardInLimitedSpace2(){
        Board b = new Board(2);
        jarvis = new IA(b, IA.Mode.TIME, 1, 1,1,2, false);
        b.makeMove(b, 0,0, Board.DIRECTIONS.TOP, 1);
        b.makeMove(b, 0,0, Board.DIRECTIONS.RIGHT, 1);
        b.makeMove(b, 0,0, Board.DIRECTIONS.LEFT, 1);
        b.makeMove(b, 0,0, Board.DIRECTIONS.BOTTOM, 1);
        b.makeMove(b, 1,0, Board.DIRECTIONS.RIGHT, 2);
        b.makeMove(b, 1,0, Board.DIRECTIONS.LEFT, 2);
        b.makeMove(b, 1,0, Board.DIRECTIONS.BOTTOM, 2);
        b.makeMove(b, 0,1, Board.DIRECTIONS.TOP, 1);
        b.makeMove(b,0,1, Board.DIRECTIONS.RIGHT, 1);
        b.makeMove(b, 1,1, Board.DIRECTIONS.BOTTOM,2);
        b.asciiPrintBoard();
        Board best = jarvis.timeMinimax();
        best.asciiPrintBoard();
    }

    @Test
    public void completeSquareIA2(){
        Board b = new Board(2);
        jarvis = new IA(b, IA.Mode.TIME, 1, 1,1,2, false);
        b.makeMove(b, 0,0, Board.DIRECTIONS.TOP, 1);
        b.makeMove(b, 0,0, Board.DIRECTIONS.RIGHT, 1);
        b.makeMove(b, 0,0, Board.DIRECTIONS.LEFT, 1);
        b.asciiPrintBoard();
        Board best = jarvis.timeMinimax();
        best.asciiPrintBoard();
    }

    @Test
    public void biggestChainEvaluatorTest(){
        Board b = new Board(5);
        jarvis = new IA(b, IA.Mode.DEPTH, 3, 1,1,2, true);
        b.makeMove(b,0,0,Board.DIRECTIONS.TOP,1);
        b.makeMove(b,0,0,Board.DIRECTIONS.LEFT,1);
        b.makeMove(b,0,1,Board.DIRECTIONS.TOP,1);
        b.makeMove(b,0,1,Board.DIRECTIONS.BOTTOM,1);
        b.makeMove(b,0,2,Board.DIRECTIONS.TOP,1);
        b.makeMove(b,0,2,Board.DIRECTIONS.BOTTOM,1);
        b.makeMove(b,0,3,Board.DIRECTIONS.TOP,1);
        b.makeMove(b,0,3,Board.DIRECTIONS.BOTTOM,1);
        b.makeMove(b,0,4,Board.DIRECTIONS.TOP,1);
        b.makeMove(b,0,4,Board.DIRECTIONS.RIGHT,1);
        b.makeMove(b,1,0,Board.DIRECTIONS.LEFT, 1);
        b.makeMove(b,1,0,Board.DIRECTIONS.BOTTOM,1);
        b.makeMove(b,1,1,Board.DIRECTIONS.BOTTOM,1);
        b.makeMove(b,1,2,Board.DIRECTIONS.BOTTOM,1);
        b.makeMove(b,1,3,Board.DIRECTIONS.RIGHT,1);
        b.makeMove(b,1,4,Board.DIRECTIONS.RIGHT, 1);
        b.makeMove(b,2,0,Board.DIRECTIONS.LEFT,1);
        b.makeMove(b,2,1,Board.DIRECTIONS.BOTTOM,1);
        b.makeMove(b,2,2,Board.DIRECTIONS.BOTTOM,1);
        b.makeMove(b,2,3,Board.DIRECTIONS.RIGHT,1);
        b.makeMove(b,2,3,Board.DIRECTIONS.BOTTOM,1);
        b.makeMove(b,2,4,Board.DIRECTIONS.RIGHT,1);
        b.makeMove(b,3,0,Board.DIRECTIONS.LEFT,1);
        b.makeMove(b,3,0,Board.DIRECTIONS.BOTTOM,1);
        b.makeMove(b,3,1,Board.DIRECTIONS.BOTTOM,1);
        b.makeMove(b,3,2,Board.DIRECTIONS.BOTTOM,1);
        b.makeMove(b,3,3,Board.DIRECTIONS.RIGHT,1);
        b.makeMove(b,3,4,Board.DIRECTIONS.RIGHT,1);
        b.makeMove(b,4,0,Board.DIRECTIONS.LEFT,1);
        b.makeMove(b,4,0,Board.DIRECTIONS.BOTTOM,1);
        b.makeMove(b,4,1,Board.DIRECTIONS.BOTTOM,1);
        b.makeMove(b,4,2,Board.DIRECTIONS.BOTTOM,1);
        b.makeMove(b,4,3,Board.DIRECTIONS.BOTTOM,1);
        b.makeMove(b,4,3,Board.DIRECTIONS.RIGHT,1);
        b.makeMove(b,4,4,Board.DIRECTIONS.RIGHT,1);
        b.makeMove(b,4,4,Board.DIRECTIONS.BOTTOM,1);
        //b.asciiPrintBoard();
        Board best = jarvis.depthMinimax();
        best.asciiPrintBoard();
    }

}
