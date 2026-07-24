/**
 * 15. Compute the difference between two dates (years, months, days).
 *
 * Test Data: 2015-01-01 to 2024-05-15
 */
import java.time.LocalDate;
import java.time.Period;
import java.util.Scanner;

public class Q15DateDifferenceYMD {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the start date (yyyy-mm-dd): ");
        LocalDate start = LocalDate.parse(scanner.nextLine());
        System.out.print("Input the end date (yyyy-mm-dd): ");
        LocalDate end = LocalDate.parse(scanner.nextLine());

        Period period = Period.between(start, end);
        System.out.println("Difference between " + start + " and " + end + ": "
                + period.getYears() + " years, " + period.getMonths() + " months, "
                + period.getDays() + " days");
    }
}
