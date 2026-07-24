/**
 * 3. Take three numbers from the user and print the greatest number.
 *
 * Test Data: 25, 78, 87 -> The greatest: 87
 */
import java.util.Scanner;

public class Q03GreatestOfThree {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the 1st number: ");
        int a = scanner.nextInt();
        System.out.print("Input the 2nd number: ");
        int b = scanner.nextInt();
        System.out.print("Input the 3rd number: ");
        int c = scanner.nextInt();

        int greatest = Math.max(a, Math.max(b, c));
        System.out.println("The greatest: " + greatest);
    }
}
