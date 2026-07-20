/**
 * 2. Compute the average of three numbers.
 *
 * Test Data: 25, 45, 65 -> The average value is 45.0
 */
public class Q02AverageOfThree {

    static double average(double a, double b, double c) {
        return (a + b + c) / 3;
    }

    public static void main(String[] args) {
        double result = average(25, 45, 65);
        System.out.println("The average value is " + result);
    }
}
