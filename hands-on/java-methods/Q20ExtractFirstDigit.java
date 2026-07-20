/**
 * 20. Extract the first digit from a positive or negative integer.
 *
 * Test Data: 1234 -> 1
 */
public class Q20ExtractFirstDigit {

    static int firstDigit(int n) {
        n = Math.abs(n);
        while (n >= 10) {
            n /= 10;
        }
        return n;
    }

    public static void main(String[] args) {
        System.out.println("Input an integer (positive/negative): 1234");
        System.out.println("Extract the first digit from the said integer: " + firstDigit(1234));
    }
}
