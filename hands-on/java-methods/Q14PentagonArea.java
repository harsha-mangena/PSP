/**
 * 14. Compute the area of a regular polygon given its number of sides and
 * side length (the sheet titles this "pentagon" but the test data is
 * generic: number of sides = 5, side = 6).
 *
 * Area = (n * s^2) / (4 * tan(pi / n))
 *
 * Test Data: sides 5, side length 6 -> The area of the pentagon is 61.93718642120281
 */
public class Q14PentagonArea {

    static double regularPolygonArea(int sides, double sideLength) {
        return (sides * sideLength * sideLength) / (4 * Math.tan(Math.PI / sides));
    }

    public static void main(String[] args) {
        System.out.println("The area of the pentagon is " + regularPolygonArea(5, 6));
    }
}
