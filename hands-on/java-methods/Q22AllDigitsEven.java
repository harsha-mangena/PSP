/**
 * 22. Check whether every digit of a given integer is even. Return true if
 * every digit is even, otherwise false. (The sheet's title text says "odd"
 * but its own worked example - 8642, all-even digits, expected output true -
 * contradicts that, so "even" is what's implemented here.)
 *
 * Test Data: 8642 -> true
 */
import java.util.Scanner;

public class Q22AllDigitsEven {

    static boolean allDigitsEven(int n) {
        n = Math.abs(n);
        if (n == 0) return true;
        while (n > 0) {
            if ((n % 10) % 2 != 0) return false;
            n /= 10;
        }
        return true;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input an integer: ");
        int n = scanner.nextInt();

        System.out.println("Check whether every digit of the said integer is even or not!");
        System.out.println(allDigitsEven(n));
    }
}
