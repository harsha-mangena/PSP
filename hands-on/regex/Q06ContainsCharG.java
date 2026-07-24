/**
 * 6. Check whether a word contains the character 'g' anywhere in it.
 */
import java.util.Scanner;

public class Q06ContainsCharG {

    static boolean containsG(String word) {
        return word.matches(".*g.*");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input a word: ");
        String word = scanner.nextLine();

        System.out.println(containsG(word));
    }
}
