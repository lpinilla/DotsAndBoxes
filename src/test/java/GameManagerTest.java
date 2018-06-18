import Backend.Board;
import Backend.GameManager;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.rules.ExpectedException;


public class GameManagerTest {

    GameManager gm;

    @Before
    public void before(){
        gm = new GameManager(3);
    }

    @Test
    public void turnChangeTest(){
        Object player1 = gm.getCurrentPlayer();
        gm.changeTurn();
        assertFalse(player1 == gm.getCurrentPlayer());
    }

}
