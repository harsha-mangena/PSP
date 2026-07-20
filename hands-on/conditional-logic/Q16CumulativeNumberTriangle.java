/**
 * 16. Display a right-angle-triangle pattern where each row concatenates the
 * numbers 1 through the row number.
 *
 * Test Data: 10 rows
 * 1
 * 12
 * 123
 * ...
 * 12345678910
 */
public class Q16CumulativeNumberTriangle {

    public static void main(String[] args) {
        int rows = 10;
        for (int i = 1; i <= rows; i++) {
            StringBuilder row = new StringBuilder();
            for (int j = 1; j <= i; j++) {
                row.append(j);
            }
            System.out.println(row);
        }
    }
}
