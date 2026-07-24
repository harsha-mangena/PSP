/**
 * 3. Concatenate a given string to the end of another string.
 *
 * Test Data: "PHP Exercises and", "Python Exercises"
 * -> The concatenated string: PHP Exercises and Python Exercises
 */
import java.util.Scanner;

public class Q03ConcatenateStrings {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the first string: ");
        String s1 = scanner.nextLine();
        System.out.print("Input the second string: ");
        String s2 = scanner.nextLine();
        String result = s1 + " " + s2;

        System.out.println("String 1: " + s1);
        System.out.println("String 2: " + s2);
        System.out.println("The concatenated string: " + result);
    }
}
