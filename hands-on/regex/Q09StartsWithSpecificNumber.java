/**
 * 9. Check whether a string starts with a specific number.
 */
public class Q09StartsWithSpecificNumber {

    static boolean startsWithNumber(String text, String number) {
        return text.matches("^" + number + ".*");
    }

    public static void main(String[] args) {
        System.out.println(startsWithNumber("5 apples in a basket", "5"));   // true
        System.out.println(startsWithNumber("123 Main Street", "123"));      // true
        System.out.println(startsWithNumber("Room 5", "5"));                  // false
    }
}
