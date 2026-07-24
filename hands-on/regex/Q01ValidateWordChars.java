/**
 * 1. Check whether a string contains only a certain set of characters
 * (a-z, A-Z and 0-9).
 *
 * Test Data:
 * "ABCDEFabcdef123450" -> true
 * "w3resource.com" -> false (contains a '.')
 */
import java.util.Scanner;

public class Q01ValidateWordChars {

    static boolean validate(String text) {
        return text.matches("^[a-zA-Z0-9]+$");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input a string: ");
        String text = scanner.nextLine();

        System.out.println(validate(text));
    }
}
