/**
 * 18. Count the number of days between two given years.
 *
 * Test Data: 2016 and 2020 (measured from Jan 1 of each year)
 */
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class Q18DaysBetweenTwoYears {

    static long daysBetweenYears(int year1, int year2) {
        LocalDate d1 = LocalDate.of(year1, 1, 1);
        LocalDate d2 = LocalDate.of(year2, 1, 1);
        return Math.abs(ChronoUnit.DAYS.between(d1, d2));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the first year: ");
        int year1 = scanner.nextInt();
        System.out.print("Input the second year: ");
        int year2 = scanner.nextInt();

        System.out.println("Number of days between " + year1 + " and " + year2 + ": "
                + daysBetweenYears(year1, year2));
    }
}
