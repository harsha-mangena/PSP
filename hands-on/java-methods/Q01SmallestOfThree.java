/**
 * 1. Find the smallest number among three numbers.
 *
 * Test Data: 25, 37, 29 -> The smallest value is 25.0
 */
import java.util.Scanner;

public class Q01SmallestOfThree {

    static double smallest(double a, double b, double c) {
        return Math.min(a, Math.min(b, c));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the first number: ");
        double a = scanner.nextDouble();
        System.out.print("Input the second number: ");
        double b = scanner.nextDouble();
        System.out.print("Input the third number: ");
        double c = scanner.nextDouble();

        System.out.println("The smallest value is " + smallest(a, b, c));
    }
}
