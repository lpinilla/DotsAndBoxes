import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.rules.ExpectedException;

public class BoardTest { //bosquejos de tests del tablero

    private class Board{  //borrar después? o mockear?
        Integer[][] matrix;

        boolean existsChain(){
            return false;
        }

        void addEdge(int x, int y, int orientation){}

        Board(int dotsPerSide){
            if(dotsPerSide <= 0) throw new IllegalArgumentException("Negative values not allowed");
            matrix = new Integer[dotsPerSide][dotsPerSide];
        }
    }

    private class Chain{}

    @Rule
    public ExpectedException excep = ExpectedException.none();

    @Test
    public void negativeValueBoardCreationTest(){
        excep.expect(IllegalArgumentException.class);
        excep.expectMessage("Negative values not allowed");
        Board b = new Board(-1);
    }

    @Test
    public void squareBoardTest(){
        Board b = new Board(5);
        assertEquals(b.matrix.length, b.matrix[0].length);
    }

    @Test
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
    }

}
