import java.util.*;

/**
 * Puzzle
 * <p>
 * At a payments processing company, optimizing incoming and outgoing payments is a type of fee optimization.
 * Below, we have a list of the incoming payments that we optimized as outgoing payments. The outgoing payments are
 * lump sums comprised of the some of the incoming payments. Write an algorithm that will show for each outgoing
 * amount which incoming amounts it is comprised of.
 * <p>
 * Identify any potential issues with your solution.
 * <p>
 * Incoming amounts:
 * 100 100 225 300 473 80
 * Outgoing amounts:
 * 180 773 225 100
 * ======================================================================
 * Created by Alex Yarotski on 5/11/2017.
 * <p>
 * This transformation is kind of lossy compression algorithm, can have multiple reverse operation.
 * Incoming amounts:
 * 1 2 3 6
 * Outgoing amounts:
 * 9 3
 * <p>
 * Another case: if the number of inputs and outputs parameters are bigger enough in order to have the dead-end
 * sequence of the numbers, exist chain of additive operations like this: 1+3 = 4;
 * where:
 * Incoming amounts:
 * 1 3 2 4
 * Outgoing amounts:
 * 4 3 3
 * and the rest of the inputs not in set of rested amounts.
 *
 * About implementation:
 *  My algorithm has limitation for cases when solution cannot be find by monotonic iteration. This case can be
 * handled interceptor block what will switch orientation from min to max and back.
 */
public class AmountsPuzzle {

  private HashMap<Integer, List<Integer>> amountSets;

  public AmountsPuzzle() {
  }

  // TODO: ??? int[] outputs, List<Integer> inputs
  public void calculate(int[] outputs, List<Integer> inputs) throws Exception {
    Map<Integer, List<Integer>> amountSets = new HashMap();
    Collections.sort(inputs);
    Arrays.sort(outputs);
    goDown(0, outputs, inputs);

    amountSets.forEach((k, v) -> System.out.println("key: " + outputs[k] + " value:" + v));
  }

  /**
   * Recursion iteration by outcoming amount list with trimming incoming numbers.
   */
  private void goDown(int outIndex, int[] outputs, List<Integer> inputs) throws Exception {
    List<Integer> selectedInputs = greedyAlgorithm(outputs[outIndex], inputs);

    amountSets.put(outIndex, selectedInputs);
    selectedInputs.forEach(n -> {
      inputs.remove(n);
    });

    if (inputs.size() > 0) {
      goDown(outIndex + 1, outputs, inputs);
    }
  }

  /**
   * Try to find sum of numbers iterating by sorted list from bigger to smaller.
   *
   * Returns list of incoming numbers which are account of the output number.
   */
  private List<Integer> reverseGreedy(int amount, List<Integer> inputs) {
    int sum = 0;
    List<Integer> result = new ArrayList<>();
    for (int i = 1; i <= inputs.size(); i++) {
      int n = inputs.get(inputs.size() - i);
      if (n < amount - sum) {
        sum += n;
        result.add(n);
      } else if (n == amount - sum) {
        result.add(n);
        return result;
      }
    }
    return null;
  }

  /**
   * Try to find sum of numbers iterating by sorted list from smaller to bigger. In case if the solution doesn't
   * exist try to change sorting to desc.
   *
   * Returns list of incoming numbers which are account of the output number.
   */
  private List<Integer> greedyAlgorithm(int amount, List<Integer> inputs) throws Exception {
    int sum = 0, length = inputs.size();
    List<Integer> result = new ArrayList<>();
    for (int i = 0; i < length; i++) {
      int n = inputs.get(i);
      if (n < amount - sum) {
        sum += n;
        result.add(n);
      } else if (n == amount - sum) {
        result.add(n);
        return result;
      } else {
        return reverseGreedy(amount, inputs);
      }
    }
    // TODO: isolated ???second extremum of F in left and right limins.
    throw new Exception( "This outgoing amount got dead-end for: " + amount + "\nthe numbers that has been left   " +
        inputs);
  }

  public static void main(String args[]) throws Exception {
    Integer[] inputs;
    int[] outputs;
    AmountsPuzzle ap = new AmountsPuzzle();

    System.out.println("=========================================================");
    inputs = new Integer[]{100, 100, 225, 300, 473, 80};
    outputs = new int[]{180, 773, 225, 100};
    ap.calculate(outputs, new ArrayList(Arrays.asList(inputs)));

    System.out.println("=========================================================");
    inputs = new Integer[]{1, 2, 3, 6};
    outputs = new int[]{9, 3};
    ap.calculate( outputs, new ArrayList(Arrays.asList(inputs)));

    System.out.println("=========================================================");
    inputs = new Integer[]{1, 2, 3, 4};
    outputs = new int[]{4, 3, 3};
    ap.calculate( outputs, new ArrayList(Arrays.asList(inputs)));

    System.out.println("=========================================================");
    inputs = new Integer[]{1, 2, 3, 4};
    outputs = new int[]{4, 3, 5};
    ap.calculate( outputs, new ArrayList(Arrays.asList(inputs)));

  }


}
