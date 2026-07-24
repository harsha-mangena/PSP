/**
 * Take a string (e.g. "hello world") and reverse its words.
 *
 * Input: "hello world" -> Output: "world hello"
 */
import java.util.Scanner;

public class Q05ReverseWords {

    static String reverseWords(String text) {
        String[] words = text.trim().split("\\s+");
        StringBuilder result = new StringBuilder();
        for (int i = words.length - 1; i >= 0; i--) {
            result.append(words[i]);
            if (i > 0) result.append(' ');
        }
        return result.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input a sentence: ");
        String text = scanner.nextLine();

        System.out.println(reverseWords(text));
    }
}
