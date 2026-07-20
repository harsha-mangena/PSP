/**
 * 15. Compute the difference between two dates (years, months, days).
 *
 * Test Data: 2015-01-01 to 2024-05-15
 */
import java.time.LocalDate;
import java.time.Period;

public class Q15DateDifferenceYMD {

    public static void main(String[] args) {
        LocalDate start = LocalDate.of(2015, 1, 1);
        LocalDate end = LocalDate.of(2024, 5, 15);

        Period period = Period.between(start, end);
        System.out.println("Difference between " + start + " and " + end + ": "
                + period.getYears() + " years, " + period.getMonths() + " months, "
                + period.getDays() + " days");
    }
}
