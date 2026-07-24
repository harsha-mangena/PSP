/**
 * 13. Calculate the area of a triangle (Heron's formula).
 *
 * Test Data: sides 10, 15, 20 -> The area of the triangle is 72.6184377413890
 */
import java.util.Scanner;

public class Q13TriangleArea {

    static double triangleArea(double a, double b, double c) {
        double s = (a + b + c) / 2;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input Side-1: ");
        double a = scanner.nextDouble();
        System.out.print("Input Side-2: ");
        double b = scanner.nextDouble();
        System.out.print("Input Side-3: ");
        double c = scanner.nextDouble();

        System.out.println("The area of the triangle is " + triangleArea(a, b, c));
    }
}
