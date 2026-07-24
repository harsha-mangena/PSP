/**
 * 9. Check whether a string starts with a specific number.
 */
import java.util.Scanner;

public class Q09StartsWithSpecificNumber {

    static boolean startsWithNumber(String text, String number) {
        return text.matches("^" + number + ".*");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input a string: ");
        String text = scanner.nextLine();
        System.out.print("Input the number it should start with: ");
        String number = scanner.nextLine();

        System.out.println(startsWithNumber(text, number));
    }
}
