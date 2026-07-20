/**
 * 9. Print characters between two characters (e.g. A to P), 20 per line.
 */
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
        printRange('A', 'P');
    }
}
