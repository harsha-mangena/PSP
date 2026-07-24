/**
 * 9. Calculate the first and last day (Monday - Sunday) of the week
 * containing a given date.
 */
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Scanner;

public class Q09FirstLastDayOfWeek {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the date (yyyy-mm-dd): ");
        LocalDate reference = LocalDate.parse(scanner.nextLine());

        LocalDate firstDay = reference.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate lastDay = reference.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        System.out.println("First day of that week: " + firstDay);
        System.out.println("Last day of that week: " + lastDay);
    }
}
