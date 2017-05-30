/**
 * Created by ubegun on 5/31/2016.
 */
/*

If X and Y are two strings of equal length N, then the difference between them is defined as the number of indices i where the i-th character of X and the i-th character of Y are different. For example, the difference between the words "ant" and "art" is 1.

You are given two Strings, A and B, where the length of A is less than or equal to the length of B. You can apply an arbitrary number of operations to A, where each operation is one of the following:

    Choose a character c and add it to the beginning of A.
    Choose a character c and add it to the end of A.

Apply the operations in such a way that A and B have the same length and the difference between them is as small as possible. Return this minimum possible difference.

Example 1

input: "chunky", "nofunky" output: 2

You can prepend "n" to "chunky" and you'll get "nchunky". The difference between
"nchunky" and "nofunky" is 2.

Example 2

input: "hello", "xello"
output: 1

Example 3

input: "adaabc", "aababbc"
output: 2

Example 4

input: "giorgi", "igroig"
output: 6
 */
public class Key extends BruteForce {

    private String longString;
    private String shortString;
    int result = 99999;


    public Key() {
        super();
    }


    public int calc(String firstKey, String secondKey) {
        int length = 0;
        if (firstKey.length() > secondKey.length()) {
            this.longString = firstKey;
            this.shortString = secondKey;
            length = firstKey.length() - secondKey.length();
        } else if (firstKey.length() < secondKey.length()) {
            this.longString = secondKey;
            this.shortString = firstKey;
            length = secondKey.length() - firstKey.length();
        } else {
            return compare(firstKey, secondKey);
        }
        super.calc(new char[]{'0', '1'}, new char[length]);
        return result;
    }

    private int compare(String firstKey, String secondKey) {
        System.out.println(firstKey + "\n" + secondKey + "\n<----->");
        if (firstKey.length() != secondKey.length()) {
            return 99999;
        }
        char[] cFirst = firstKey.toCharArray();
        char[] cSecond = secondKey.toCharArray();
        int count = 0;
        for (int i = 0; i < firstKey.length(); i++) {
            if (cFirst[i] != cSecond[i]) {
                count++;
            }
        }
        return count;
    }

    private StringBuffer truncStr;

    @Override
    void testString(String testKey) {
        truncStr = new StringBuffer(longString);
        for (char c : testKey.toCharArray()) {
            switch (c) {
                case '0':
                    //System.out.println("left shift");
                    truncStr.deleteCharAt(0);
                    break;
                case '1':
                    //System.out.println("right shift");
                    truncStr.deleteCharAt(truncStr.length() - 1);
                    break;
                default:
                    System.out.println("What?");
            }
        }
        int ret = compare(truncStr.toString(), shortString);
        if(ret < result){
            result = ret;
        }
        //System.out.println("---------------------");
    }
}
