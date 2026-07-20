/**
 * 6. Check whether a word contains the character 'g' anywhere in it.
 */
public class Q06ContainsCharG {

    static boolean containsG(String word) {
        return word.matches(".*g.*");
    }

    public static void main(String[] args) {
        System.out.println(containsG("go"));       // true - starts with g
        System.out.println(containsG("egg"));      // true - middle
        System.out.println(containsG("bag"));      // true - ends with g
        System.out.println(containsG("apple"));    // false
    }
}
