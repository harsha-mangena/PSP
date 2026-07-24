/**
 * 8. Take a single character from the alphabet and print Vowel or Consonant.
 * If the input is not a letter, or is a string of length > 1, print an
 * error message.
 *
 * Test Data: "p" -> Input letter is Consonant
 */
import java.util.Scanner;

public class Q08VowelOrConsonant {

    static String classify(String input) {
        if (input.length() != 1 || !Character.isLetter(input.charAt(0))) {
            return "Error: input must be a single letter (a-z or A-Z)";
        }
        char c = Character.toLowerCase(input.charAt(0));
        boolean isVowel = "aeiou".indexOf(c) >= 0;
        return "Input letter is " + (isVowel ? "Vowel" : "Consonant");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input an alphabet: ");
        String input = scanner.nextLine();

        System.out.println(classify(input));
    }
}
