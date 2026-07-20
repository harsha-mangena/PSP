/**
 * 8. Match a string that contains only upper and lowercase letters, numbers,
 * and underscores.
 */
public class Q08UpperLowerDigitUnderscoreOnly {

    static boolean matches(String text) {
        return text.matches("^\\w+$");
    }

    public static void main(String[] args) {
        System.out.println(matches("Valid_Name_123"));  // true
        System.out.println(matches("Also_Valid"));      // true
        System.out.println(matches("Not Valid"));        // false - contains a space
        System.out.println(matches("Not-Valid"));        // false - contains a hyphen
    }
}
