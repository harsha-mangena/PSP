/**
 * 9. Print characters between two characters (e.g. A to P), 20 per line.
 */
import java.util.Scanner;

public class Q09PrintCharacterRange {

    static void printRange(char from, char to) {
        int count = 0;
        for (char c = from; c <= to; c++) {
            System.out.print(c);
            count++;
            if (count % 20 == 0) {
                System.out.println();
            }
        }
        if (count % 20 != 0) System.out.println();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the starting character: ");
        char from = scanner.next().charAt(0);
        System.out.print("Input the ending character: ");
        char to = scanner.next().charAt(0);

        printRange(from, to);
    }
}
