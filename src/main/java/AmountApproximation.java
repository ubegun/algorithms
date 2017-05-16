/**
 * Created by ubegun on 6/1/2016.
 */
public class AmountApproximation {


    double r1 = 1;
    double r2 = 1;

    private void calcRatio(int p1, int p2) {
        if(p1 < p2){
            r1 = (double) p1 / (double) p2;
            r2 = 1 - r1;
        }else if( p1 > p2){
            r2 = (double) p2 / (double) p1;
            r1 = 1 - r2;
        }
    }

    private double approximate(double ratio, int coins, int amount) {
        return ((double) amount * ratio / (double) coins);
    }


    public int calc(int p1, int p2, int amount) {
        calcRatio(p1, p2);
        int left = (int) approximate(r1, p1, amount);
        int right = (int) approximate(r2, p2, amount);
        int first = left * p1 + right * p2;
        int second = (left + 1) * p1 + (right - 1) * p2;
        int third = (left - 1) * p1 + (right + 1) * p2;
        System.out.println( "first:" + first + " second:" + second + " third:" + third);
        System.out.println("left:" + left + " right:" + right);
        return 0;
    }

}
