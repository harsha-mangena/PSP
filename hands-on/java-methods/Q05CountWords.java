/**
 * 5. Count all words in a string.
 *
 * Test Data: "The quick brown fox jumps over the lazy dog." -> 9
 */
import java.util.Scanner;

public class Q05CountWords {

    static int countWords(String text) {
        String trimmed = text.trim();
        if (trimmed.isEmpty()) return 0;
        return trimmed.split("\\s+").length;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the string: ");
        String text = scanner.nextLine();

        System.out.println("Number of words in the string: " + countWords(text));
    }
}
