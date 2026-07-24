/**
 * 1. Get a number from the user and print whether it is positive or negative.
 *
 * Test Data: 35 -> Number is positive
 */
import java.util.Scanner;

public class Q01PositiveOrNegative {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input number: ");
        int number = scanner.nextInt();

        if (number > 0) {
            System.out.println("Number is positive");
        } else if (number < 0) {
            System.out.println("Number is negative");
        } else {
            System.out.println("Number is zero");
        }
    }
}
