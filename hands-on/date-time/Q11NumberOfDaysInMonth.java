/**
 * 11. Get the number of days of a month.
 *
 * Test Data: month 2 (February), year 2016 -> 29 days
 */
import java.time.YearMonth;
import java.util.Scanner;

public class Q11NumberOfDaysInMonth {

    static int daysInMonth(int year, int month) {
        return YearMonth.of(year, month).lengthOfMonth();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input a month number: ");
        int month = scanner.nextInt();
        System.out.print("Input a year: ");
        int year = scanner.nextInt();

        System.out.println("Number of days in month " + month + "/" + year + ": " + daysInMonth(year, month));
    }
}
