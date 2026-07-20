/**
 * 13. Display the cube of the numbers up to a given integer.
 *
 * Test Data: 4 -> Number is : 1 and cube of 1 is : 1
 *                 Number is : 2 and cube of 2 is : 8
 *                 Number is : 3 and cube of 3 is : 27
 *                 Number is : 4 and cube of 4 is : 64
 */
public class Q13CubesUpToN {

    public static void main(String[] args) {
        int n = 4;
        for (int i = 1; i <= n; i++) {
            System.out.println("Number is : " + i + " and cube of " + i + " is : " + (i * i * i));
        }
    }
}
