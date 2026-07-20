/**
 * 6. Compute the sum of the digits in an integer.
 *
 * Test Data: 25 -> The sum is 7
 */
public class Q06SumOfDigits {

    static int sumOfDigits(int n) {
        n = Math.abs(n);
        int sum = 0;
        while (n > 0) {
            sum += n % 10;
            n /= 10;
        }
        return sum;
    }

    public static void main(String[] args) {
        System.out.println("The sum is " + sumOfDigits(25));
    }
}
