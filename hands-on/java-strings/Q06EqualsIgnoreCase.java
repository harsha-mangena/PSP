/**
 * 6. Compare a given string to another string, ignoring case considerations.
 *
 * Test Data: "Stephen Edwin King" equals "stephen edwin king"? true
 */
import java.util.Scanner;

public class Q06EqualsIgnoreCase {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the first string: ");
        String s1 = scanner.nextLine();
        System.out.print("Input the second string: ");
        String s2 = scanner.nextLine();

        System.out.println("\"" + s1 + "\" equals \"" + s2 + "\"? " + s1.equalsIgnoreCase(s2));
    }
}
