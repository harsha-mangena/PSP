/**
 * 4. Read a floating-point number and print "zero" if it is zero, otherwise
 * "positive" or "negative". Add "small" if the absolute value is less than
 * 1, or "large" if it exceeds 1,000,000.
 *
 * Test Data: 25 -> Input value: 25 / Positive number
 */
import java.util.Scanner;

public class Q04ClassifyFloatingPointNumber {

    static void classify(double value) {
        System.out.println("Input value: " + value);

        if (value == 0) {
            System.out.println("Zero");
            return;
        }

        StringBuilder result = new StringBuilder();
        double abs = Math.abs(value);
        if (abs < 1) {
            result.append("Small ");
        } else if (abs > 1_000_000) {
            result.append("Large ");
        }
        result.append(value > 0 ? "Positive number" : "Negative number");
        System.out.println(result);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input a number: ");
        double value = scanner.nextDouble();

        classify(value);
    }
}
