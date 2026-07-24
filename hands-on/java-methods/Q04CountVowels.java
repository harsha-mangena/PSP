/**
 * 4. Count all vowels in a string.
 *
 * Test Data: "w3resource" -> Number of Vowels in the string: 4
 */
import java.util.Scanner;

public class Q04CountVowels {

    static int countVowels(String text) {
        int count = 0;
        for (char c : text.toLowerCase().toCharArray()) {
            if ("aeiou".indexOf(c) >= 0) count++;
        }
        return count;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the string: ");
        String text = scanner.nextLine();

        System.out.println("Number of Vowels in the string: " + countVowels(text));
    }
}
