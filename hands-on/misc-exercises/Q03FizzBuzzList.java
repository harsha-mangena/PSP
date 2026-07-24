/**
 * Iterate through a list of integers. If divisible by 3, print "Fizz". If
 * divisible by 5, print "Buzz". If divisible by both, print "FizzBuzz".
 * Otherwise print the number itself.
 *
 * Input: 12 5 15 7
 * Output:
 * Fizz
 * Buzz
 * FizzBuzz
 * 7
 */
import java.util.Scanner;

public class Q03FizzBuzzList {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the numbers, separated by spaces: ");
        String[] tokens = scanner.nextLine().trim().split("\\s+");

        for (String token : tokens) {
            int n = Integer.parseInt(token);
            if (n % 3 == 0 && n % 5 == 0) {
                System.out.println("FizzBuzz");
            } else if (n % 3 == 0) {
                System.out.println("Fizz");
            } else if (n % 5 == 0) {
                System.out.println("Buzz");
            } else {
                System.out.println(n);
            }
        }
    }
}
