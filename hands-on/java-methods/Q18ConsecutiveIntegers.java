/**
 * 18. Accept three integers and check whether they are consecutive or not.
 *
 * Test Data: 15, 16, 17 -> true
 */
import java.util.Arrays;

public class Q18ConsecutiveIntegers {

    static boolean areConsecutive(int a, int b, int c) {
        int[] sorted = {a, b, c};
        Arrays.sort(sorted);
        return sorted[1] == sorted[0] + 1 && sorted[2] == sorted[1] + 1;
    }

    public static void main(String[] args) {
        System.out.println("Check whether the three said numbers are consecutive or not!"
                + areConsecutive(15, 16, 17));
    }
}
