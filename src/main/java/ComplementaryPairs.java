import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Algorithm for finding K-complementary pairs in a given array of integers.
 * The result the evaluation by Time and Space are:
 *  T(n) = O(n)
 *  S(n) = O(3*n)
 *
 * Created by Alex Yarotski on 2/9/17.
 */
public final class ComplementaryPairs {

  /**
   * Main method expected like parameters interval of numbers and K-complementary in the following order:
   * args[0] - from (includes this number).
   * args[1] - to (includes this number).
   * args[2] - K-complementary.
   *
   * @param args command line parameters.
   */
  public static void main(final String... args) {
    if (args.length == 3) {
      //parse parameters
      int from = Integer.parseInt(args[0]);
      int to = Integer.parseInt(args[1]);
      int k = Integer.parseInt(args[2]);

      //Prepares the array of integer number by given interval.
      ArrayList<Integer> interval = new ArrayList<>();
      for(int i = from; i <= to; i++){
        interval.add(i);
      }
      int[] numbers = interval.stream().mapToInt(i->i).toArray();

      long duration = 0;
      boolean isPalindrome = false;
      long startTime = System.currentTimeMillis();
      //Pares calculation.
      int[][] pairs = getComplementaryPairs(numbers, k);
      duration = System.currentTimeMillis() - startTime;
      System.out.println("Calculation time: " + duration + " ns.\n");

      for (int[] thePairs : pairs) {
        System.out.println(" Pairs are " + thePairs[0] + " and " + thePairs[1]);
      }
    } else {
      System.err.print("No arguments!");
    }
  }

  /**
   * For computing pairs of numbers satisfying K = A [i] + A [j]; I used HashMap. This is the most
   * effective option for this case. As result the evaluation by Time and Space are:
   * T(n) = O(n)
   * S(n) = O(3*n)
   */
  public static int[][] getComplementaryPairs(int[] input, final int k) {


    Integer pare = null;
    int[][] collector = new int[input.length][2];

    /**
     * Indexing the set of data using HashMap, using expected number like a Key for each number.
     * (T)ime and (S)pace evaluations for this operation are:
     *  T(n) = O(n)
     *  S(n) = O(3*n)
     */
    HashMap<Integer, Integer> index = Arrays.stream(input).
        collect(HashMap::new, (m, c) -> m.put(k - c, c), (m, u) -> {
        });

    /**
     * Find pares.
     * In this block, the most costly operation iterates over the original set of numbers.
     * And (T)ime and (S)pace evaluations for this operation are:
     * T(n) = O(n)
     * S(n) = O(n)
     */
    int count = 0;
    for (int num : input) {
      pare = index.remove(num);
      if (pare != null) {
        collector[count][0] = num;
        collector[count][1] = pare;
        count++;
      }
    }

    /**
     * Trimming the result set.
     * T(n) = O(n)
     * S(n) = O(n)
     */
    int[][] result = new int[count][2];
    for (int i = 0; i < count; i++) {
      result[i][0] = collector[i][0];
      result[i][1] = collector[i][1];
    }

    return result;
  }

}
