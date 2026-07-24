/**
 * 6. Read in two floating-point numbers and test whether they are the same
 * up to three decimal places.
 *
 * Test Data: 25.586 and 25.589 -> They are different
 */
import java.util.Scanner;

public class Q06CompareFloatsToThreeDecimals {

    static boolean sameToThreeDecimals(double a, double b) {
        double rounded1 = Math.round(a * 1000.0) / 1000.0;
        double rounded2 = Math.round(b * 1000.0) / 1000.0;
        return rounded1 == rounded2;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input floating-point number: ");
        double a = scanner.nextDouble();
        System.out.print("Input floating-point another number: ");
        double b = scanner.nextDouble();

        System.out.println(sameToThreeDecimals(a, b) ? "They are the same" : "They are different");
    }
}
