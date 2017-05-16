import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by ubegun on 5/27/2016.
 */
public class LuckyStringTest {

    @Test
    public void calcTest() throws Exception {
        LuckyString ls = new LuckyString();

       Assert.assertEquals("ab = 2", ls.calc("ab") , 2);
       Assert.assertEquals("aaa = 0", ls.calc("aaa") , 0);
        Assert.assertEquals("aaab = 0 ", ls.calc("aaab") , 0);
        Assert.assertEquals("3628800 = 1" ,ls.calc("0123") , LuckyString.factorial(4));
        Assert.assertEquals("abababa = 1" ,ls.calc("aaaabbb") , 1);
       Assert.assertEquals("abcdefghij = 3628800", ls.calc("abcdefghij") , 3628800);
    }

}