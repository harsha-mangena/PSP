/**
 * 20. Extract the first digit from a positive or negative integer.
 *
 * Test Data: 1234 -> 1
 */
import java.util.Scanner;

public class Q20ExtractFirstDigit {

    static int firstDigit(int n) {
        n = Math.abs(n);
        while (n >= 10) {
            n /= 10;
        }
        return n;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input an integer (positive/negative): ");
        int n = scanner.nextInt();

        System.out.println("Extract the first digit from the said integer: " + firstDigit(n));
    }
}
