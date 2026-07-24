/**
 * 4. Check whether a given string ends with the contents of another string.
 *
 * Test Data: "Python Exercises" ends with "se"? false
 *            "Python Exercise" ends with "se"? true
 */
import java.util.Scanner;

public class Q04EndsWith {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the string: ");
        String text = scanner.nextLine();
        System.out.print("Input the suffix to check: ");
        String suffix = scanner.nextLine();

        System.out.println("\"" + text + "\" ends with \"" + suffix + "\"? " + text.endsWith(suffix));
    }
}
