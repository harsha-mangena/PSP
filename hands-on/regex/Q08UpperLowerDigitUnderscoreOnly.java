/**
 * 8. Match a string that contains only upper and lowercase letters, numbers,
 * and underscores.
 */
import java.util.Scanner;

public class Q08UpperLowerDigitUnderscoreOnly {

    static boolean matches(String text) {
        return text.matches("^\\w+$");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input a string: ");
        String text = scanner.nextLine();

        System.out.println(matches(text));
    }
}
