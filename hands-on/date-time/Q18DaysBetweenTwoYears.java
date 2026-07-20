/**
 * 18. Count the number of days between two given years.
 *
 * Test Data: 2016 and 2020 (measured from Jan 1 of each year)
 */
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Q18DaysBetweenTwoYears {

    static long daysBetweenYears(int year1, int year2) {
        LocalDate d1 = LocalDate.of(year1, 1, 1);
        LocalDate d2 = LocalDate.of(year2, 1, 1);
        return Math.abs(ChronoUnit.DAYS.between(d1, d2));
    }

    public static void main(String[] args) {
        System.out.println("Number of days between 2016 and 2020: " + daysBetweenYears(2016, 2020));
    }
}
