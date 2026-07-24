/**
 * 7. Match a word containing 'g', but not at the start or end of the word.
 *
 * \B is a non-word-boundary: "\\Bg\\B" only matches a 'g' that has letters on
 * both sides of it.
 */
import java.util.Scanner;
import java.util.regex.Pattern;

public class Q07ContainsGNotAtEnds {

    private static final Pattern PATTERN = Pattern.compile("\\Bg\\B");

    static boolean hasInteriorG(String word) {
        return PATTERN.matcher(word).find();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input a word: ");
        String word = scanner.nextLine();

        System.out.println(hasInteriorG(word));
    }
}
