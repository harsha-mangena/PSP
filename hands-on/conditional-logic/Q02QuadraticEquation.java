/**
 * 2. Solve quadratic equations (use if, else if and else).
 *
 * Test Data: a=1, b=5, c=1
 * Expected Output: The roots are -0.20871215252208009 and -4.791287847477919
 */
import java.util.Scanner;

public class Q02QuadraticEquation {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input a: ");
        double a = scanner.nextDouble();
        System.out.print("Input b: ");
        double b = scanner.nextDouble();
        System.out.print("Input c: ");
        double c = scanner.nextDouble();

        double discriminant = b * b - 4 * a * c;

        if (discriminant > 0) {
            double root1 = (-b + Math.sqrt(discriminant)) / (2 * a);
            double root2 = (-b - Math.sqrt(discriminant)) / (2 * a);
            System.out.println("The roots are " + root1 + " and " + root2);
        } else if (discriminant == 0) {
            double root = -b / (2 * a);
            System.out.println("The root is " + root);
        } else {
            double realPart = -b / (2 * a);
            double imaginaryPart = Math.sqrt(-discriminant) / (2 * a);
            System.out.println("The roots are complex: " + realPart + " + " + imaginaryPart
                    + "i and " + realPart + " - " + imaginaryPart + "i");
        }
    }
}
