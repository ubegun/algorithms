/**
 * Implements algorithm for checking if a string is a palindrome.
 *
 * Created by Alex Yarotski on 2/9/17.
 */
public final class Palindrome {

  /**
   * As parameters expected an array of strings for testing by palindrome algorithm.
   */
  public static void main(final String... args) {
    if (args.length > 0) {
      for (String testString : args) {
        System.out.println("Test string:" + testString);
        long duration = 0;
        boolean isPalindrome = false;
        long startTime = System.nanoTime();
        //Calculation.
        isPalindrome = isPalindrome(testString.toCharArray());
        duration = System.nanoTime() - startTime;
        System.out.println("Is palindrome: " + isPalindrome);
        System.out.println("Calculation time: " + duration + " ns.\n");
      }
    } else {
      System.err.print("No arguments!");
    }
  }

  /**
   * From evaluation of time: T(n) = O(n)
   * From evaluation of space: S(n) = O(n)
   *
   * @param value is input char array
   * @return true if this char array is the palindrome, otherwise false.
   */
  public static boolean isPalindrome(char[] value) {
    int length = value.length;
    for (int i = 0; i < length / 2; i++) {
      if (value[i] != value[length - i - 1]) {
        return false;
      }
    }
    return true;
  }

}
