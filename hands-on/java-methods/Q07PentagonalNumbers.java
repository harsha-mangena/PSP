/**
 * 7. Display the first N pentagonal numbers (worksheet demos N = 50).
 *
 * P(n) = n(3n-1)/2. First terms: 1, 5, 12, 22, 35, 51, 70, 92, 117, 145, ...
 * which matches the scanned answer key exactly, printed 10 per line.
 */
import java.util.Scanner;

public class Q07PentagonalNumbers {

    static long pentagonal(int n) {
        return (long) n * (3L * n - 1) / 2;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("How many pentagonal numbers to display: ");
        int count = scanner.nextInt();

        for (int n = 1; n <= count; n++) {
            System.out.print(pentagonal(n));
            System.out.print((n % 10 == 0) ? "\n" : "\t");
        }
        if (count % 10 != 0) System.out.println();
    }
}
