/**
 * 13. Get the day of the week of a specific date.
 *
 * Test Data: 2017-01-25 -> Wednesday
 */
import java.time.LocalDate;
import java.util.Scanner;

public class Q13DayOfWeekOfSpecificDate {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the date (yyyy-mm-dd): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());

        System.out.println(date + " is a " + date.getDayOfWeek());
    }
}
