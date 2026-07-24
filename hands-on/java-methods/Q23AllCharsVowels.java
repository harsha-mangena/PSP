/**
 * 23. Check whether all the characters in a given string are vowels
 * (a, e, i, o, u). Return true if every character is a vowel, otherwise
 * false.
 */
import java.util.Scanner;

public class Q23AllCharsVowels {

    static boolean allVowels(String text) {
        for (char c : text.toLowerCase().toCharArray()) {
            if ("aeiou".indexOf(c) < 0) return false;
        }
        return !text.isEmpty();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input a string: ");
        String text = scanner.nextLine();

        System.out.println("Check all the characters of the said string are Vowels or not!");
        System.out.println(allVowels(text));
    }
}
