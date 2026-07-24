/**
 * 6. Compute the sum of the digits in an integer.
 *
 * Test Data: 25 -> The sum is 7
 */
import java.util.Scanner;

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
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input an integer: ");
        int n = scanner.nextInt();

        System.out.println("The sum is " + sumOfDigits(n));
    }
}
