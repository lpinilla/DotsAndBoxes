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
    public void squareSharedBooleanEdgeCorrectness(){
        excep.expect(RuntimeException.class);
        excep.expectMessage("already an edge");
        b.addEdge(0,0, Board.DIRECTIONS.RIGHT);
        b.addEdge(0, 1, Board.DIRECTIONS.LEFT);
    }


    @Test
    public void addEgdeToExistingEdgeTest(){
        excep.expect(RuntimeException.class);
        excep.expectMessage("already an edge");
        b.addEdge(0,0, Board.DIRECTIONS.TOP);
        b.addEdge(0,0, Board.DIRECTIONS.TOP);
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
                        continue;
                    }
                }
            }
        }
        assertFalse(b.hasRemainingPlays());
    }

    //Chain stuff


    /*@Test
    public void chainVerificationOnEmptyMap(){
        Board b = new Board(3);
        assertFalse(b.existsChain());
    }

    @Test
    public void chainVerificationwithEdgesNoChains(){
        Board b = new Board(3);
        b.addEdge( 1, 2, 2);
        b.addEdge( 3, 4, 1);
        assertFalse(b.existsChain());
    }*/

}
