/**
 * 10. Get the name of the first and last day (weekday name) of a month.
 */
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;

public class Q10FirstLastDayNameOfMonth {

    public static void main(String[] args) {
        YearMonth ym = YearMonth.now();
        LocalDate first = ym.atDay(1);
        LocalDate last = ym.atEndOfMonth();

        System.out.println("First day of the month (" + first + ") is a "
                + first.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()));
        System.out.println("Last day of the month (" + last + ") is a "
                + last.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()));
    }
}
