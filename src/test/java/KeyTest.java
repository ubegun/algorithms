import org.junit.Assert;
import org.junit.Test;

/**
 * Created by ubegun on 5/31/2016.
 */
public class KeyTest {

    enum Shift{LEFT, RIGHT};

    @Test
    public void calcTest() throws Exception {
        Key sc = new Key();
        Assert.assertEquals("input: \"chunky\", \"nofunky\" output: 2 ", 2, sc.calc("chunky", "nofunky"));
    }

}