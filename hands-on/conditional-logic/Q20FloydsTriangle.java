/**
 * 20. Print Floyd's Triangle.
 *
 * Test Data: 5 rows
 * 1
 * 2 3
 * 4 5 6
 * 7 8 9 10
 * 11 12 13 14 15
 */
public class Q20FloydsTriangle {

    public static void main(String[] args) {
        int rows = 5;
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
