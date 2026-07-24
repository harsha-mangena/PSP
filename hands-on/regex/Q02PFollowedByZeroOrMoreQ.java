/**
 * 2. Match a string that has a 'p' followed by zero or more 'q's.
 */
import java.util.Scanner;

public class Q02PFollowedByZeroOrMoreQ {

    static boolean isPFollowedByZeroOrMoreQ(String text) {
        return text.matches("pq*");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input a string: ");
        String text = scanner.nextLine();

        System.out.println(isPFollowedByZeroOrMoreQ(text));
    }
}
