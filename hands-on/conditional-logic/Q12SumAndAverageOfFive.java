/**
 * 12. Input 5 numbers from the keyboard and find their sum and average.
 *
 * Test Data: 1 2 3 4 5 -> The Sum of 5 no is : 15 / The Average is : 3.0
 */
import java.util.Scanner;

public class Q12SumAndAverageOfFive {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] numbers = new int[5];
        System.out.println("Input the 5 numbers:");
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = scanner.nextInt();
        }

        int sum = 0;
        for (int n : numbers) sum += n;
        double average = sum / (double) numbers.length;

        System.out.println("The Sum of 5 no is : " + sum);
        System.out.println("The Average is : " + average);
    }
}
