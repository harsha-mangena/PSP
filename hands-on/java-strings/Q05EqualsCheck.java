/**
 * 5. Check whether two String objects contain the same data.
 *
 * Test Data: "Stephen Edwin King" equals "Walter Winchell"? false
 */
import java.util.Scanner;

public class Q05EqualsCheck {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the first string: ");
        String s1 = scanner.nextLine();
        System.out.print("Input the second string: ");
        String s2 = scanner.nextLine();

        System.out.println("\"" + s1 + "\" equals \"" + s2 + "\"? " + s1.equals(s2));
    }
}
