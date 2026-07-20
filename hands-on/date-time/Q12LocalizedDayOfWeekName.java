/**
 * 12. Get localized day-in-week name.
 */
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class Q12LocalizedDayOfWeekName {

    public static void main(String[] args) {
        LocalDate today = LocalDate.now();
        System.out.println("Today (" + today + ") is a "
                + today.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()));
        System.out.println("In French: "
                + today.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRENCH));
    }
}
