/**
 * Given an array of String values, find the longest string and print it.
 *
 * Input: ["apple", "banana", "kiwi"] -> Output: banana
 */
public class Q04LongestString {

    static String longest(String[] words) {
        String result = "";
        for (String word : words) {
            if (word.length() > result.length()) {
                result = word;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        String[] words = {"apple", "banana", "kiwi"};
        System.out.println(longest(words));
    }
}
