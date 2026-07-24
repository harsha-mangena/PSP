/**
 * Given a string, determine if it is a palindrome (reads the same forwards
 * and backward, ignoring spaces, punctuation and case).
 *
 * Input: "A man, a plan, a canal: Panama" -> Output: true
 */
import java.util.Scanner;

public class Q06PalindromeCheck {

    static boolean isPalindrome(String text) {
        String cleaned = text.toLowerCase().replaceAll("[^a-z0-9]", "");
        return cleaned.equals(new StringBuilder(cleaned).reverse().toString());
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input a string: ");
        String text = scanner.nextLine();

        System.out.println(isPalindrome(text));
    }
}
