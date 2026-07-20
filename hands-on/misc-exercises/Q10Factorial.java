/**
 * Calculate the factorial of a number (e.g. 5! = 5*4*3*2*1 = 120).
 *
 * Input: 5 -> Output: 120
 */
public class Q10Factorial {

    static long factorial(int n) {
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(factorial(5));
    }
}
