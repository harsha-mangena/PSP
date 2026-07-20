/**
 * 1. Check whether a string contains only a certain set of characters
 * (a-z, A-Z and 0-9).
 *
 * Test Data:
 * "ABCDEFabcdef123450" -> true
 * "SQL" -> true
 * "Java" -> true
 * "*&^%$#!(" -> false
 * "w3resource.com" -> false (contains a '.')
 */
public class Q01ValidateWordChars {

    static boolean validate(String text) {
        return text.matches("^[a-zA-Z0-9]+$");
    }

    public static void main(String[] args) {
        System.out.println(validate("ABCDEFabcdef123450"));
        System.out.println(validate("SQL"));
        System.out.println(validate("Java"));
        System.out.println(validate("*&^%$#!("));
        System.out.println(validate("w3resource.com"));
    }
}
