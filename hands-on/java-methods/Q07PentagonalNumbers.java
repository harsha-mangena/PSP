/**
 * 7. Display the first 50 pentagonal numbers.
 *
 * P(n) = n(3n-1)/2. First terms: 1, 5, 12, 22, 35, 51, 70, 92, 117, 145, ...
 * which matches the scanned answer key exactly, printed 10 per line.
 */
public class Q07PentagonalNumbers {

    static long pentagonal(int n) {
        return (long) n * (3L * n - 1) / 2;
    }

    public static void main(String[] args) {
        for (int n = 1; n <= 50; n++) {
            System.out.print(pentagonal(n));
            System.out.print((n % 10 == 0) ? "\n" : "\t");
        }
    }
}
