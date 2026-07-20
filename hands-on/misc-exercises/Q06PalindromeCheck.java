/**
 * Given a string, determine if it is a palindrome (reads the same forwards
 * and backward, ignoring spaces, punctuation and case).
 *
 * Input: "A man, a plan, a canal: Panama" -> Output: true
 */
public class Q06PalindromeCheck {

    static boolean isPalindrome(String text) {
        String cleaned = text.toLowerCase().replaceAll("[^a-z0-9]", "");
        return cleaned.equals(new StringBuilder(cleaned).reverse().toString());
    }

    public static void main(String[] args) {
        System.out.println(isPalindrome("A man, a plan, a canal: Panama"));
    }
}
