/**
 * 12. Take a number n as input and display an n-by-n matrix of random 0/1
 * values. (Output is random by nature, so there is no fixed expected value
 * to match - this demonstrates the generator with n = 10.)
 */
import java.util.Random;

public class Q12RandomMatrix {

    static int[][] randomMatrix(int n) {
        Random random = new Random();
        int[][] matrix = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = random.nextInt(2);
            }
        }
        return matrix;
    }

    static void print(int[][] matrix) {
        for (int[] row : matrix) {
            StringBuilder sb = new StringBuilder();
            for (int value : row) {
                sb.append(value).append(' ');
            }
            System.out.println(sb.toString().trim());
        }
    }

    public static void main(String[] args) {
        System.out.println("Input a number: 10");
        print(randomMatrix(10));
    }
}
