/**
 * 1. Get the character at a given index within a string.
 *
 * Test Data: "Java Exercises!" position 0 -> J, position 10 -> i
 */
import java.util.Scanner;

public class Q01CharAtIndex {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input a string: ");
        String text = scanner.nextLine();
        System.out.print("Input the position: ");
        int position = scanner.nextInt();

        System.out.println("Original String = " + text);
        System.out.println("The character at position " + position + " is " + text.charAt(position));
    }
}
