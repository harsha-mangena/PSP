/**
 * 1. Get the character at a given index within a string.
 *
 * Original String = "Java Exercises!"
 * The character at position 0 is J
 * The character at position 10 is i
 */
public class Q01CharAtIndex {

    public static void main(String[] args) {
        String text = "Java Exercises!";
        System.out.println("Original String = " + text);
        System.out.println("The character at position 0 is " + text.charAt(0));
        System.out.println("The character at position 10 is " + text.charAt(10));
    }
}
