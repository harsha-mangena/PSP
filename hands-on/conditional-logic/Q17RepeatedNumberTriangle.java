/**
 * 17. Display a right-angle-triangle pattern where each row repeats the row
 * number that many times.
 *
 * Expected pattern:
 * 1
 * 22
 * 333
 * 4444
 */
public class Q17RepeatedNumberTriangle {

    public static void main(String[] args) {
        int rows = 4;
        for (int i = 1; i <= rows; i++) {
            System.out.println(String.valueOf(i).repeat(i));
        }
    }
}
