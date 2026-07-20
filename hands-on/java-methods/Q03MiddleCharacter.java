/**
 * 3. Display the middle character(s) of a string.
 *
 * Rule: odd length -> one middle character; even length -> two middle
 * characters. (The scanned sheet's note has the two swapped, but its own
 * worked example - "350" is odd-length and returns a single character -
 * matches this rule, so that is what's implemented.)
 *
 * Test Data: "350" -> The middle character in the string: 5
 */
public class Q03MiddleCharacter {

    static String middle(String text) {
        int len = text.length();
        int mid = len / 2;
        if (len % 2 == 0) {
            return text.substring(mid - 1, mid + 1);
        }
        return String.valueOf(text.charAt(mid));
    }

    public static void main(String[] args) {
        System.out.println("The middle character in the string: " + middle("350"));
        System.out.println("The middle character in the string: " + middle("w3resource"));
    }
}
