/**
 * 10. Display the first N natural numbers (worksheet demos N = 10).
 */
import java.util.Scanner;

public class Q10FirstTenNaturalNumbers {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input how many natural numbers to display: ");
        int count = scanner.nextInt();

        System.out.println("The first " + count + " natural numbers are:");
        for (int i = 1; i <= count; i++) {
            System.out.println(i);
        }
    }
}
