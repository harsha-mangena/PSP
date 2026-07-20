/**
 * 1. Get a number from the user and print whether it is positive or negative.
 *
 * Test Data: 35 -> Number is positive
 */
public class Q01PositiveOrNegative {

    public static void main(String[] args) {
        int number = 35;
        if (number > 0) {
            System.out.println("Number is positive");
        } else if (number < 0) {
            System.out.println("Number is negative");
        } else {
            System.out.println("Number is zero");
        }
    }
}
