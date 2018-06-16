import Backend.Board;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.rules.ExpectedException;

import java.util.Set;

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
    public void makeMoveToExistingEdgeTest(){
        excep.expect(RuntimeException.class);
        excep.expectMessage("already an edge");
        b.makeMove(b,0,0, Board.DIRECTIONS.TOP,1);
        b.makeMove(b,0,0, Board.DIRECTIONS.TOP,1);
    }

    @Test
    public void squareSharedBooleanEdgeCorrectness(){
        excep.expect(RuntimeException.class);
        excep.expectMessage("already an edge");
        b.makeMove(b,0,0, Board.DIRECTIONS.RIGHT,1);
        b.makeMove(b,0, 1, Board.DIRECTIONS.LEFT,1);
    }

    @Test
    public void squareSharedBooleanEdgeCorrectness2(){
        excep.expect(RuntimeException.class);
        excep.expectMessage("already an edge");
        b.makeMove(b,0,1, Board.DIRECTIONS.BOTTOM,1);
        b.makeMove(b,1, 1, Board.DIRECTIONS.TOP,1);
    }

    @Test
    public void squareSharedBooleanEdgeCorrectness3(){
        excep.expect(RuntimeException.class);
        excep.expectMessage("already an edge");
        b.makeMove(b,1,1, Board.DIRECTIONS.TOP,1);
        b.makeMove(b,0, 1, Board.DIRECTIONS.BOTTOM,1);
    }

    @Test
    public void squareSharedBooleanEdgeCorrectness4(){
        excep.expect(RuntimeException.class);
        excep.expectMessage("already an edge");
        b.makeMove(b,1, 1, Board.DIRECTIONS.LEFT,1);
        b.makeMove(b,1,0, Board.DIRECTIONS.RIGHT,1);
    }

    @Test
    public void fillBoxTest(){
        excep.expect(IllegalArgumentException.class);
        excep.expectMessage("Already painted!");
        b.makeMove(b,0,0, Board.DIRECTIONS.TOP,1);
        b.makeMove(b,0,0, Board.DIRECTIONS.BOTTOM,1);
        b.makeMove(b,0,0, Board.DIRECTIONS.LEFT,1);
        b.makeMove(b,0,0, Board.DIRECTIONS.RIGHT,1);
        b.colorBox(0,0,1);
    }


    /*Test para saber si se comparten bien las aristas. Si se comparten bien,
     se debería llenar un cuadrado en el cual nunca se le llenaron las aristas
     en el mismo directamente.*/
    @Test
    public void fillBoxByConsequence(){
        excep.expect(IllegalArgumentException.class);
        excep.expectMessage("Already painted!");
        b.makeMove(b,1,2, Board.DIRECTIONS.LEFT,1);
        b.makeMove(b,1,0, Board.DIRECTIONS.RIGHT,1);
        b.makeMove(b,2,1, Board.DIRECTIONS.TOP,1);
        b.makeMove(b,0,1, Board.DIRECTIONS.BOTTOM,1);
        b.colorBox(1,1,1);
    }

    /*Test para saber si funciona bien el manejo de turnos.
    tapamos las exceptions por multiples aristas para poder testear lo planteado.*/
    @Test
    public void noMorePlaysTests(){
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for(Board.DIRECTIONS dir : Board.DIRECTIONS.values()){
                    try {
                        b.makeMove(b,i, j, dir,1);
                    }catch(RuntimeException e){
                        e = null; //descartar exception
                    }
                }
            }
        }
        assertFalse(b.hasRemainingPlays(b));
    }

    //Heuristic Tests

    @Test
    public void evaluateTest0(){
        assertEquals(0, b.numberOfCapturableBoxes());
    }

    @Test
    public void evaluateTest1(){
        b.makeMove(b,0,0, Board.DIRECTIONS.TOP, 1);
        b.makeMove(b,0,0, Board.DIRECTIONS.LEFT, 1);
        b.makeMove(b,0,0, Board.DIRECTIONS.RIGHT, 1);
        b.makeMove(b,1,0, Board.DIRECTIONS.LEFT, 1);
        b.makeMove(b,1,1, Board.DIRECTIONS.BOTTOM, 1);
        b.makeMove(b,0,1, Board.DIRECTIONS.BOTTOM, 1);
        assertEquals(1, b.numberOfCapturableBoxes());
    }

    @Test
    public void evaluateTest2(){
        Board b2 = new Board(2);
        b2.makeMove(b2,0,0, Board.DIRECTIONS.TOP, 1);
        b2.makeMove(b2,0,0, Board.DIRECTIONS.LEFT, 1);
        b2.makeMove(b2,0,0, Board.DIRECTIONS.RIGHT, 1);
        b2.makeMove(b2,1,0, Board.DIRECTIONS.LEFT, 1);
        b2.makeMove(b2,0,1, Board.DIRECTIONS.BOTTOM, 1);
        b2.makeMove(b2,1,1, Board.DIRECTIONS.BOTTOM, 1);
        b2.makeMove(b2,1,1, Board.DIRECTIONS.RIGHT, 1);
        assertEquals(3, b2.numberOfCapturableBoxes());
    }

    @Test
    public void evaluateTest3(){
        b.makeMove(b,0,0, Board.DIRECTIONS.TOP, 1);
        b.makeMove(b,0,0, Board.DIRECTIONS.RIGHT, 1);
        b.makeMove(b,0,0, Board.DIRECTIONS.LEFT, 1);
        b.makeMove(b,0,0, Board.DIRECTIONS.BOTTOM, 1);
        assertEquals(0, b.numberOfCapturableBoxes());
    }

    //Possible moves tests

    @Test
    public void getNeighborTest1(){ //tal vez no ande con n == 3
        Board b2 = new Board(2);
        b2.makeMove(b2,0,0, Board.DIRECTIONS.TOP,1);
        b2.makeMove(b2,0,0, Board.DIRECTIONS.LEFT,1);
        b2.makeMove(b2,0,0, Board.DIRECTIONS.RIGHT,1);
        b2.makeMove(b2,0,0, Board.DIRECTIONS.BOTTOM,1);
        b2.makeMove(b2,0,1, Board.DIRECTIONS.TOP,1);
        b2.makeMove(b2,0,1, Board.DIRECTIONS.BOTTOM,1);
        b2.makeMove(b2,0,1, Board.DIRECTIONS.RIGHT,1);
        b2.makeMove(b2,1,0, Board.DIRECTIONS.BOTTOM,1);
        b2.makeMove(b2,1,0, Board.DIRECTIONS.LEFT,1);
        b2.makeMove(b2,1,0,Board.DIRECTIONS.RIGHT,1);
        b2.makeMove(b2,1,1,Board.DIRECTIONS.BOTTOM, 1);
        assertEquals(1,b2.getPossibleMoves2(b2, 3).size());
    }

    @Test
    public void getNeighborTest2(){
        b.makeMove(b,0,0, Board.DIRECTIONS.TOP,1);
        b.makeMove(b,0,0,Board.DIRECTIONS.LEFT,1);
        b.makeMove(b,0,0,Board.DIRECTIONS.RIGHT,1);
        Set<Board> moves = b.getPossibleMoves(b, 3);
        assertTrue(b.getPossibleMoves(b, 3).size() != 0); //dio 40, ni idea si esta bien
    }

    @Test
    public void getNeighborTest3(){
        Board b2 = new Board(2);
        b2.makeMove(b2,0,0, Board.DIRECTIONS.TOP, 1);
        b2.makeMove(b2,0,0,Board.DIRECTIONS.LEFT, 1);
        b2.makeMove(b2,0,0,Board.DIRECTIONS.RIGHT, 1);
        b2.makeMove(b2,0,0,Board.DIRECTIONS.BOTTOM, 1);
        assertEquals(8,b2.getPossibleMoves(b2, 3).size());
    }

    @Test
    public void getNeighborTest4(){
        Board b2 = new Board(2);
        b2.makeMove(b2,0,0, Board.DIRECTIONS.TOP, 1);
        b2.makeMove(b2,0,0,Board.DIRECTIONS.LEFT, 1);
        b2.makeMove(b2,0,0,Board.DIRECTIONS.RIGHT, 1);
        Set<Board> moves = b2.getPossibleMoves(b2, 3);
        assertEquals(16,b2.getPossibleMoves(b2, 3).size());
    }

}
