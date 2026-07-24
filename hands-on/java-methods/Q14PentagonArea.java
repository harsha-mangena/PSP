/**
 * 14. Compute the area of a regular polygon given its number of sides and
 * side length.
 *
 * Area = (n * s^2) / (4 * tan(pi / n))
 *
 * Test Data: sides 5, side length 6 -> The area of the pentagon is 61.93718642120281
 */
import java.util.Scanner;

public class Q14PentagonArea {

    static double regularPolygonArea(int sides, double sideLength) {
        return (sides * sideLength * sideLength) / (4 * Math.tan(Math.PI / sides));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the number of sides: ");
        int sides = scanner.nextInt();
        System.out.print("Input the side: ");
        double side = scanner.nextDouble();

        System.out.println("The area of the pentagon is " + regularPolygonArea(sides, side));
    }
}
