/**
 * 16. Find all twin prime numbers below a given limit (worksheet demos 100).
 *
 * Twin primes are pairs of primes that differ by 2.
 *
 * Test Data: 100 ->
 * (3, 5) (5, 7) (11, 13) (17, 19) (29, 31) (41, 43) (59, 61) (71, 73)
 */
import java.util.Scanner;

public class Q16TwinPrimes {

    static boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; (long) i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the upper limit: ");
        int limit = scanner.nextInt();

        for (int n = 2; n < limit - 2; n++) {
            if (isPrime(n) && isPrime(n + 2)) {
                System.out.println("(" + n + ", " + (n + 2) + ")");
            }
        }
    }
}
