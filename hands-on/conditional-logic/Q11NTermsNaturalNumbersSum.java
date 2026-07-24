/**
 * 11. Display n terms of natural numbers and their sum.
 */
import java.util.Scanner;

public class Q11NTermsNaturalNumbersSum {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the number: ");
        int n = scanner.nextInt();
        int sum = 0;

        System.out.println("The first " + n + " natural numbers are:");
        for (int i = 1; i <= n; i++) {
            System.out.println(i);
            sum += i;
        }
        System.out.println("The Sum of Natural Number upto " + n + " terms: " + sum);
    }
}
