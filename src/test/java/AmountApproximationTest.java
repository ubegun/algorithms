import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by ubegun on 6/1/2016.
 */
public class AmountApproximationTest {
    @Test
    public void calc() throws Exception {
        AmountApproximation aa = new AmountApproximation();

        aa.calc(7, 9, 17); // 18
        aa.calc(7, 13, 17); // 20
        aa.calc(7, 13, 21); // 21
        aa.calc(9, 17, 37); // 43
        aa.calc(2345, 7253, 287398); //287398

    }

}