import Backend.GameManager;
import Backend.IA;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IATest {

    IA jarvis;
    GameManager gm;

    @Before
    public void before(){
        gm = new GameManager();
        jarvis = new IA(gm.getBoard(), IA.Mode.DEPTH, 3);
    }
}
