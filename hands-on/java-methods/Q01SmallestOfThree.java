/**
 * 1. Find the smallest number among three numbers.
 *
 * Test Data: 25, 37, 29 -> The smallest value is 25.0
 */
public class Q01SmallestOfThree {

    static double smallest(double a, double b, double c) {
        return Math.min(a, Math.min(b, c));
    }

    public static void main(String[] args) {
        double result = smallest(25, 37, 29);
        System.out.println("The smallest value is " + result);
    }
}
