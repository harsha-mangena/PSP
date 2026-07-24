/**
 * 15. Display n terms of odd natural numbers and their sum.
 *
 * Test Data: 5 terms -> 1 3 5 7 9, sum 25
 */
import java.util.Scanner;

public class Q15OddNumbersAndSum {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input number of terms: ");
        int terms = scanner.nextInt();
        int sum = 0;

        System.out.println("The odd numbers are :");
        int number = 1;
        for (int i = 0; i < terms; i++) {
            System.out.println(number);
            sum += number;
            number += 2;
        }
        System.out.println("The Sum of odd Natural Number upto " + terms + " terms is: " + sum);
    }
}
