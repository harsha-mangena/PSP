/**
 * 7. Get the last day of a given month (worksheet demos "the current month";
 * generalized here to any year/month the user provides).
 */
import java.time.YearMonth;
import java.util.Scanner;

public class Q07LastDayOfCurrentMonth {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the year: ");
        int year = scanner.nextInt();
        System.out.print("Input the month (1-12): ");
        int month = scanner.nextInt();

        System.out.println("Last day of that month: " + YearMonth.of(year, month).atEndOfMonth());
    }
}
