/**
 * 12. Get the localized day-of-week name for a given date.
 */
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Scanner;

public class Q12LocalizedDayOfWeekName {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the date (yyyy-mm-dd): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());

        System.out.println(date + " is a "
                + date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()));
        System.out.println("In French: "
                + date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRENCH));
    }
}
