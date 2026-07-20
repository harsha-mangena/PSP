/**
 * 12. Input 5 numbers from the keyboard and find their sum and average.
 *
 * Test Data: 1 2 3 4 5 -> The Sum of 5 no is : 15 / The Average is : 3.0
 */
public class Q12SumAndAverageOfFive {

    public static void main(String[] args) {
        int[] numbers = {1, 2, 3, 4, 5};
        int sum = 0;
        for (int n : numbers) sum += n;
        double average = sum / (double) numbers.length;

        System.out.println("The Sum of 5 no is : " + sum);
        System.out.println("The Average is : " + average);
    }
}
