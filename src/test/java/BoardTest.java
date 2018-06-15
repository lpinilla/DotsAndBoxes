import Backend.Board;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.rules.ExpectedException;

public class BoardTest {

    private Board b;
    private int size = 3;

    @Before
    public void before(){
        b = new Board(size);
    }

    @Rule
    public ExpectedException excep = ExpectedException.none();

    @Test
    public void negativeValueBoardCreationTest(){
        excep.expect(IllegalArgumentException.class);
        excep.expectMessage("Zero or negative values not allowed");
        Board b = new Board(-1);
    }

    @Test
    public void addEdgeToExistingEdgeTest(){
        excep.expect(RuntimeException.class);
        excep.expectMessage("already an edge");
        b.addEdge(0,0, Board.DIRECTIONS.TOP);
        b.addEdge(0,0, Board.DIRECTIONS.TOP);
    }

    @Test
    public void squareSharedBooleanEdgeCorrectness(){
        excep.expect(RuntimeException.class);
        excep.expectMessage("already an edge");
        b.addEdge(0,0, Board.DIRECTIONS.RIGHT);
        b.addEdge(0, 1, Board.DIRECTIONS.LEFT);
    }

    @Test
    public void squareSharedBooleanEdgeCorrectness2(){
        excep.expect(RuntimeException.class);
        excep.expectMessage("already an edge");
        b.addEdge(0,1, Board.DIRECTIONS.BOTTOM);
        b.addEdge(1, 1, Board.DIRECTIONS.TOP);
    }

    @Test
    public void squareSharedBooleanEdgeCorrectness3(){
        excep.expect(RuntimeException.class);
        excep.expectMessage("already an edge");
        b.addEdge(1,1, Board.DIRECTIONS.TOP);
        b.addEdge(0, 1, Board.DIRECTIONS.BOTTOM);
    }

    @Test
    public void squareSharedBooleanEdgeCorrectness4(){
        excep.expect(RuntimeException.class);
        excep.expectMessage("already an edge");
        b.addEdge(1, 1, Board.DIRECTIONS.LEFT);
        b.addEdge(1,0, Board.DIRECTIONS.RIGHT);
    }

    @Test
    public void fillBoxTest(){
        b.addEdge(0,0, Board.DIRECTIONS.TOP);
        b.addEdge(0,0, Board.DIRECTIONS.BOTTOM);
        b.addEdge(0,0, Board.DIRECTIONS.LEFT);
        b.addEdge(0,0, Board.DIRECTIONS.RIGHT);
        assertTrue(b.isSquareFilled(0,0));
    }


    /*Test para saber si se comparten bien las aristas. Si se comparten bien,
     se debería llenar un cuadrado en el cual nunca se le llenaron las aristas
     en el mismo directamente.*/
    @Test
    public void fillBoxByConsequence(){
        b.addEdge(1,2, Board.DIRECTIONS.LEFT);
        b.addEdge(1,0, Board.DIRECTIONS.RIGHT);
        b.addEdge(2,1, Board.DIRECTIONS.TOP);
        b.addEdge(0,1, Board.DIRECTIONS.BOTTOM);
        assertTrue(b.isSquareFilled(1,1));
    }

    /*Test para saber si funciona bien el manejo de turnos.
    tapamos las exceptions por multiples aristas para poder testear lo planteado.*/
    @Test
    public void noMorePlaysTests(){
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for(Board.DIRECTIONS dir : Board.DIRECTIONS.values()){
                    try {
                        b.addEdge(i, j, dir);
                    }catch(RuntimeException e){
                        e = null; //descartar exception
                    }
                }
            }
        }
        assertFalse(b.hasRemainingPlays());
    }

    //Heuristic Tests

    @Test
    public void evaluateTest0(){
        assertEquals(0, b.numberOfCapturableBoxes());
    }

    @Test
    public void evaluateTest1(){
        b.addEdge(0,0, Board.DIRECTIONS.TOP);
        b.addEdge(0,0, Board.DIRECTIONS.LEFT);
        b.addEdge(0,0, Board.DIRECTIONS.RIGHT);
        b.addEdge(1,0, Board.DIRECTIONS.LEFT);
        b.addEdge(1,1, Board.DIRECTIONS.BOTTOM);
        b.addEdge(0,1, Board.DIRECTIONS.BOTTOM);
        assertEquals(1, b.numberOfCapturableBoxes());
    }

    @Test
    public void evaluateTest2(){
        Board b2 = new Board(2);
        b2.addEdge(0,0, Board.DIRECTIONS.TOP);
        b2.addEdge(0,0, Board.DIRECTIONS.LEFT);
        b2.addEdge(0,0, Board.DIRECTIONS.RIGHT);
        b2.addEdge(1,0, Board.DIRECTIONS.LEFT);
        b2.addEdge(0,1, Board.DIRECTIONS.BOTTOM);
        b2.addEdge(1,1, Board.DIRECTIONS.BOTTOM);
        b2.addEdge(1,1, Board.DIRECTIONS.RIGHT);
        assertEquals(3, b2.numberOfCapturableBoxes());
    }

    @Test
    public void evaluateTest3(){
        b.addEdge(0,0, Board.DIRECTIONS.TOP);
        b.addEdge(0,0, Board.DIRECTIONS.RIGHT);
        b.addEdge(0,0, Board.DIRECTIONS.LEFT);
        b.addEdge(0,0, Board.DIRECTIONS.BOTTOM);
        assertEquals(0, b.numberOfCapturableBoxes());
    }

    //Possible moves tests

    @Test
    public void getNeighborTest1(){
        Board b2 = new Board(2);
        b2.addEdge(0,0, Board.DIRECTIONS.TOP);
        b2.addEdge(0,0, Board.DIRECTIONS.LEFT);
        b2.addEdge(0,0, Board.DIRECTIONS.RIGHT);
        b2.addEdge(0,0, Board.DIRECTIONS.BOTTOM);
        b2.addEdge(0,1, Board.DIRECTIONS.TOP);
        b2.addEdge(0,1, Board.DIRECTIONS.BOTTOM);
        b2.addEdge(0,1, Board.DIRECTIONS.RIGHT);
        b2.addEdge(1,0, Board.DIRECTIONS.BOTTOM);
        b2.addEdge(1,0, Board.DIRECTIONS.LEFT);
        b2.addEdge(1,0,Board.DIRECTIONS.RIGHT);
        b2.addEdge(1,1,Board.DIRECTIONS.BOTTOM);
        b2.asciiPrintBoard();
        assertEquals(1,b2.getPossibleMoves(b2, 3).size());
    }

}
