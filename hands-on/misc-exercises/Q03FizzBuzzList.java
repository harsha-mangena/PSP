/**
 * Iterate through a List of integers. If divisible by 3, print "Fizz". If
 * divisible by 5, print "Buzz". If divisible by both, print "FizzBuzz".
 * Otherwise print the number itself.
 *
 * Input: [12, 5, 15, 7]
 * Output: Fizz / Buzz / FizzBuzz / 7
 */
import java.util.List;

public class Q03FizzBuzzList {

    public static void main(String[] args) {
        List<Integer> numbers = List.of(12, 5, 15, 7);

        for (int n : numbers) {
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
