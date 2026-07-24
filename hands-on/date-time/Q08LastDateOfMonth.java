/**
 * 8. Get the last date (day-of-month number) of a given month.
 */
import java.time.YearMonth;
import java.util.Scanner;

public class Q08LastDateOfMonth {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the year: ");
        int year = scanner.nextInt();
        System.out.print("Input the month (1-12): ");
        int month = scanner.nextInt();

        YearMonth ym = YearMonth.of(year, month);
        System.out.println("That month has " + ym.lengthOfMonth() + " days.");
        System.out.println("The last date of the month is: " + ym.atEndOfMonth().getDayOfMonth());
    }
}
