import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;


/**
 * Created by ubegun on 5/27/2016.
 * <p>
 * A string is called lucky if no two consecutive characters are equal.
 * How many lucky strings can you get by reordering letters in a given string *s*?
 * s* itself counts, too, if it is lucky.
 * <p>
 * 1 <= length(*s*) <= 10
 * s[i]* is in 'a'..'z'
 * <p>
 * Example 1
 * <p>
 * input: "ab"
 * output: 2
 * <p>
 * Two lucky strings - "ab" and "ba".
 * <p>
 * Example 2
 * <p>
 * input: "aaab"
 * output: 0
 * <p>
 * It's impossible to construct a lucky string.
 * <p>
 * Example 3
 * <p>
 * input: "aabbbaa"
 * output: 1
 * <p>
 * "abababa" is the only lucky string that can be generated.
 * <p>
 * Example 4
 * <p>
 * input: "abcdefghij"
 * output: 3628800
 */
public class LuckyString {

    private Set<String> luckyStr;


    public static int factorial(int i) {
        if (i < 1) return 1;
        return factorial(i - 1) * i;
    }

    public boolean feelLucky(String s) {
        //System.out.println(s);
        char undo = '*';
        for (char c : s.toCharArray()) {
            if (c == undo) {
                return false;
            }
            undo = c;
        }
        return true;
    }


    private void forEachComb(StringBuffer testStr, char[] thisChars, Consumer<char[]> variant) {
        final char thisChar = testStr.charAt(0);
        if (testStr.length() == 1) {
            for (int i = 0; i < thisChars.length; i++) {
                thisChars[i] = thisChar;
                variant.accept(thisChars);
                thisChars[i] = 0;
            }
            return;
        }
        testStr.deleteCharAt(0);
        forEachComb(testStr, thisChars, n -> {
            for (int i = 0; i < thisChars.length; i++) {
                if (n[i] == 0) {
                    n[i] = thisChar;
                    variant.accept(n);
                    n[i] = 0;
                }
            }
        });
    }


    int count;


    public int calc(String inStr) {
        count = 0;
        luckyStr = new HashSet<String>();
        final char[] testSequ = new char[inStr.length()];
        forEachComb(new StringBuffer(inStr), testSequ, n -> {
            String variant = new String(n);
            if (feelLucky(variant)) {
                count++;
                luckyStr.add(variant);
            }
        });
        System.out.println(luckyStr.size() + "   " + count);
        return luckyStr.size();
    }

}
