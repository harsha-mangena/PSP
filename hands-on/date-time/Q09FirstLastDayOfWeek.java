/**
 * 9. Calculate the first and last day of the current week (Monday - Sunday).
 */
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class Q09FirstLastDayOfWeek {

    public static void main(String[] args) {
        LocalDate today = LocalDate.now();
        LocalDate firstDay = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate lastDay = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        System.out.println("First day of this week: " + firstDay);
        System.out.println("Last day of this week: " + lastDay);
    }
}
