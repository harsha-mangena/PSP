/**
 * 3. Concatenate a given string to the end of another string.
 *
 * String 1: "PHP Exercises and"
 * String 2: "Python Exercises"
 * The concatenated string: PHP Exercises and Python Exercises
 */
public class Q03ConcatenateStrings {

    public static void main(String[] args) {
        String s1 = "PHP Exercises and";
        String s2 = "Python Exercises";
        String result = s1 + " " + s2;

        System.out.println("String 1: " + s1);
        System.out.println("String 2: " + s2);
        System.out.println("The concatenated string: " + result);
    }
}
