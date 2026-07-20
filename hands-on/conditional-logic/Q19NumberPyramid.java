/**
 * 19. Display a pyramid pattern with a number repeated in each row, the
 * number matching the row index.
 *
 * Expected pattern:
 *    1
 *   2 2
 *  3 3 3
 * 4 4 4 4
 */
public class Q19NumberPyramid {

    public static void main(String[] args) {
        int rows = 4;
        for (int i = 1; i <= rows; i++) {
            StringBuilder row = new StringBuilder();
            row.append(" ".repeat(rows - i));
            for (int j = 1; j <= i; j++) {
                row.append(i);
                if (j < i) row.append(' ');
            }
            System.out.println(row);
        }
    }
}
