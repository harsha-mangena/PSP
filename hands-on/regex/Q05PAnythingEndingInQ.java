/**
 * 5. Match a string that has a 'p', followed by anything, ending in 'q'.
 */
import java.util.Scanner;

public class Q05PAnythingEndingInQ {

    static boolean matches(String text) {
        return text.matches("p.*q");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input a string: ");
        String text = scanner.nextLine();

        System.out.println(matches(text));
    }
}
