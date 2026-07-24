/**
 * 18. Accept three integers and check whether they are consecutive or not.
 *
 * Test Data: 15, 16, 17 -> true
 */
import java.util.Arrays;
import java.util.Scanner;

public class Q18ConsecutiveIntegers {

    static boolean areConsecutive(int a, int b, int c) {
        int[] sorted = {a, b, c};
        Arrays.sort(sorted);
        return sorted[1] == sorted[0] + 1 && sorted[2] == sorted[1] + 1;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the first number: ");
        int a = scanner.nextInt();
        System.out.print("Input the second number: ");
        int b = scanner.nextInt();
        System.out.print("Input the third number: ");
        int c = scanner.nextInt();

        System.out.println("Check whether the three said numbers are consecutive or not!"
                + areConsecutive(a, b, c));
    }
}
