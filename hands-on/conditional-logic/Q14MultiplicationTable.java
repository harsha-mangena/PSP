/**
 * 14. Display the multiplication table of a given integer.
 *
 * Test Data: number 5, terms 5
 * 5 X 0 = 0
 * 5 X 1 = 5
 * 5 X 2 = 10
 * 5 X 3 = 15
 * 5 X 4 = 20
 * 5 X 5 = 25
 */
public class Q14MultiplicationTable {

    public static void main(String[] args) {
        int number = 5, terms = 5;
        for (int i = 0; i <= terms; i++) {
            System.out.println(number + " X " + i + " = " + (number * i));
        }
    }
}
