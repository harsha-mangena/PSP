/**
 * Given a list of String values, find the longest string and print it.
 *
 * Input: apple banana kiwi -> Output: banana
 */
import java.util.Scanner;

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
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the words, separated by spaces: ");
        String[] words = scanner.nextLine().trim().split("\\s+");

        System.out.println(longest(words));
    }
}
