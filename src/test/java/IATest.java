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
        Board best = jarvis.depthMinimax();
    }

    @Test
    public void completeSquareIA(){
        Board b = new Board(2);
        jarvis = new IA(b, IA.Mode.DEPTH, 1, 0,1,2, false);
        b.makeMove(b, 0,0, Board.DIRECTIONS.TOP, 1);
        b.makeMove(b, 0,0, Board.DIRECTIONS.RIGHT, 1);
        b.makeMove(b, 0,0, Board.DIRECTIONS.LEFT, 1);
        Board best = jarvis.depthMinimax();
        best.asciiPrintBoard();
    }
}
