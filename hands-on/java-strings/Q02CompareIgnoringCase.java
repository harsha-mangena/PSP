/**
 * 2. Compare two strings lexicographically, ignoring case differences.
 *
 * String 1: "This is exercise 1"
 * String 2: "This is Exercise 1"
 * -> equal
 */
public class Q02CompareIgnoringCase {

    public static void main(String[] args) {
        String s1 = "This is exercise 1";
        String s2 = "This is Exercise 1";

        System.out.println("String 1: " + s1);
        System.out.println("String 2: " + s2);

        if (s1.compareToIgnoreCase(s2) == 0) {
            System.out.println("\"" + s1 + "\" is equal to \"" + s2 + "\"");
        } else {
            System.out.println("\"" + s1 + "\" is not equal to \"" + s2 + "\"");
        }
    }
}
