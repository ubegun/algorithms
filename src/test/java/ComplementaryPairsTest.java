import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * Created by <br> on 2/9/17.
 */
public class ComplementaryPairsTest {


  @Test
  public void getComplementaryPairs() throws Exception {
    final int[] input = new int[]{1, 2, 3, 5, 6, 8, 9};
    final int k = 6;

    Map<Integer, Integer> index = Arrays.stream(input).collect(HashMap::new, (m, c) -> m.put(k - c, c), (m, u) -> {
    });
    assertTrue(index.size() > 0);
  }

}