/**
 * 2. Compare two strings lexicographically, ignoring case differences.
 *
 * Test Data: "This is exercise 1", "This is Exercise 1" -> equal
 */
import java.util.Scanner;

public class Q02CompareIgnoringCase {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the first string: ");
        String s1 = scanner.nextLine();
        System.out.print("Input the second string: ");
        String s2 = scanner.nextLine();

        System.out.println("String 1: " + s1);
        System.out.println("String 2: " + s2);

        if (s1.compareToIgnoreCase(s2) == 0) {
            System.out.println("\"" + s1 + "\" is equal to \"" + s2 + "\"");
        } else {
            System.out.println("\"" + s1 + "\" is not equal to \"" + s2 + "\"");
        }
    }
}
