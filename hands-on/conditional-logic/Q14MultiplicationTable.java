/**
 * 14. Display the multiplication table of a given integer.
 *
 * Test Data: number 5, terms 5
 * 5 X 0 = 0
 * 5 X 1 = 5
 * ...
 * 5 X 5 = 25
 */
import java.util.Scanner;

public class Q14MultiplicationTable {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the number (Table to be calculated): ");
        int number = scanner.nextInt();
        System.out.print("Input number of terms: ");
        int terms = scanner.nextInt();

        for (int i = 0; i <= terms; i++) {
            System.out.println(number + " X " + i + " = " + (number * i));
        }
    }
}
