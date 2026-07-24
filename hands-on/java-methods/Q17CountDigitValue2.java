/**
 * 17. Count the number of digits in a non-negative integer that have the
 * value 2.
 *
 * Test Data: 12541 -> 1
 */
import java.util.Scanner;

public class Q17CountDigitValue2 {

    static int countTwos(int n) {
        int count = 0;
        while (n > 0) {
            if (n % 10 == 2) count++;
            n /= 10;
        }
        return count;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input a number: ");
        int n = scanner.nextInt();

        System.out.println(countTwos(n));
    }
}
