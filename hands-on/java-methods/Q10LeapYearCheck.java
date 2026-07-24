/**
 * 10. Check whether a year (integer) is a leap year or not.
 *
 * Test Data: 2017 -> false
 */
import java.util.Scanner;

public class Q10LeapYearCheck {

    static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input a year: ");
        int year = scanner.nextInt();

        System.out.println(isLeapYear(year));
    }
}
