/**
 * 17. Count the number of digits in a non-negative integer that have the
 * value 2.
 *
 * Test Data: 12541 -> 1
 */
public class Q17CountDigitValue2 {

    static int countTwos(int n) {
        int count = 0;
        while (n > 0) {
            if (n % 10 == 2) count++;
            n /= 10;
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println("Input a number: 12541");
        System.out.println(countTwos(12541));
    }
}
