/**
 * 16. Find all twin prime numbers less than 100.
 *
 * Twin primes are pairs of primes that differ by 2.
 *
 * Expected Output:
 * (3, 5) (5, 7) (11, 13) (17, 19) (29, 31) (41, 43) (59, 61) (71, 73)
 */
public class Q16TwinPrimes {

    static boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; (long) i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        for (int n = 2; n < 98; n++) {
            if (isPrime(n) && isPrime(n + 2)) {
                System.out.println("(" + n + ", " + (n + 2) + ")");
            }
        }
    }
}
