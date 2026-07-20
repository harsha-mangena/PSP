/**
 * 3. Take three numbers from the user and print the greatest number.
 *
 * Test Data: 25, 78, 87 -> The greatest: 87
 */
public class Q03GreatestOfThree {

    public static void main(String[] args) {
        int a = 25, b = 78, c = 87;
        int greatest = Math.max(a, Math.max(b, c));
        System.out.println("The greatest: " + greatest);
    }
}
