/**
 * 22. Check whether every digit of a given integer is even. Return true if
 * every digit is even, otherwise false. (The sheet's title text says "odd"
 * but its own worked example - 8642, all-even digits, expected output true -
 * contradicts that, so "even" is what's implemented here.)
 *
 * Test Data: 8642 -> true
 */
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
        System.out.println("Input an integer: 8642");
        System.out.println("Check whether every digit of the said integer is even or not!");
        System.out.println(allDigitsEven(8642));
    }
}
