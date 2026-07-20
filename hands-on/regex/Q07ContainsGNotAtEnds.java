/**
 * 7. Match a word containing 'g', but not at the start or end of the word.
 *
 * \B is a non-word-boundary: "\\Bg\\B" only matches a 'g' that has letters on
 * both sides of it.
 */
import java.util.regex.Pattern;

public class Q07ContainsGNotAtEnds {

    private static final Pattern PATTERN = Pattern.compile("\\Bg\\B");

    static boolean hasInteriorG(String word) {
        return PATTERN.matcher(word).find();
    }

    public static void main(String[] args) {
        System.out.println(hasInteriorG("bagel"));   // true - g is interior
        System.out.println(hasInteriorG("egg"));     // true - middle g qualifies
        System.out.println(hasInteriorG("go"));      // false - g is at the start
        System.out.println(hasInteriorG("bag"));     // false - g is at the end
        System.out.println(hasInteriorG("apple"));   // false - no g at all
    }
}
