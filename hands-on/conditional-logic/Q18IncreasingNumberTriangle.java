/**
 * 18. Display a right-angle-triangle pattern with numbers increasing by 1
 * across the whole triangle.
 *
 * Expected pattern:
 * 1
 * 2 3
 * 4 5 6
 * 7 8 9 10
 */
public class Q18IncreasingNumberTriangle {

    public static void main(String[] args) {
        int rows = 4;
        int counter = 1;
        for (int i = 1; i <= rows; i++) {
            StringBuilder row = new StringBuilder();
            for (int j = 1; j <= i; j++) {
                row.append(counter++);
                if (j < i) row.append(' ');
            }
            System.out.println(row);
        }
    }
}
