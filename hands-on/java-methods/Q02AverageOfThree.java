/**
 * 2. Compute the average of three numbers.
 *
 * Test Data: 25, 45, 65 -> The average value is 45.0
 */
import java.util.Scanner;

public class Q02AverageOfThree {

    static double average(double a, double b, double c) {
        return (a + b + c) / 3;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the first number: ");
        double a = scanner.nextDouble();
        System.out.print("Input the second number: ");
        double b = scanner.nextDouble();
        System.out.print("Input the third number: ");
        double c = scanner.nextDouble();

        System.out.println("The average value is " + average(a, b, c));
    }
}
