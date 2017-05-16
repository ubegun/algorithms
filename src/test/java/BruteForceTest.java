import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by ubegun on 5/31/2016.
 */
public class BruteForceTest {

    @Test
    public void calcTest() throws Exception {
        (new BruteForce(){

            int variants = 0;

            @Override
            void testString(String testKey) {
                System.out.println(testKey + "#" + variants++);
            }
        }).calc("01".toCharArray(), new char[4]);
    }

}