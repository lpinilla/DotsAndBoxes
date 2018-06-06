import Backend.Board;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.rules.ExpectedException;

public class BoardTest { //bosquejos de tests del tablero

    @Rule
    public ExpectedException excep = ExpectedException.none();

    @Test
    public void negativeValueBoardCreationTest(){
        excep.expect(IllegalArgumentException.class);
        excep.expectMessage("Zero or negative values not allowed");
        Board b = new Board(-1);
    }

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
