/**
 * Take two strings and return true if they are anagrams (contain the same
 * letters in a different order).
 *
 * Input: "listen", "silent" -> Output: true
 */
import java.util.Arrays;

public class Q11AnagramCheck {

    static boolean areAnagrams(String a, String b) {
        char[] chars1 = a.toLowerCase().replaceAll("\\s", "").toCharArray();
        char[] chars2 = b.toLowerCase().replaceAll("\\s", "").toCharArray();
        Arrays.sort(chars1);
        Arrays.sort(chars2);
        return Arrays.equals(chars1, chars2);
    }

    public static void main(String[] args) {
        System.out.println(areAnagrams("listen", "silent"));
    }
}
