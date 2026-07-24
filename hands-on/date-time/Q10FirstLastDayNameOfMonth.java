/**
 * 10. Get the name of the first and last day (weekday name) of a given month.
 */
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Scanner;

public class Q10FirstLastDayNameOfMonth {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the year: ");
        int year = scanner.nextInt();
        System.out.print("Input the month (1-12): ");
        int month = scanner.nextInt();

        YearMonth ym = YearMonth.of(year, month);
        LocalDate first = ym.atDay(1);
        LocalDate last = ym.atEndOfMonth();

        System.out.println("First day of the month (" + first + ") is a "
                + first.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()));
        System.out.println("Last day of the month (" + last + ") is a "
                + last.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()));
    }
}
