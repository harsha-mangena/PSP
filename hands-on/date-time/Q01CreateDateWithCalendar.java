/**
 * 1. Create a Date object using the Calendar class.
 */
import java.util.Calendar;
import java.util.Scanner;

public class Q01CreateDateWithCalendar {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the year: ");
        int year = scanner.nextInt();
        System.out.print("Input the month (1-12): ");
        int month = scanner.nextInt();
        System.out.print("Input the day: ");
        int date = scanner.nextInt();

        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1); // Calendar months are 0-based.
        cal.set(Calendar.DATE, date);

        System.out.println(cal.getTime());
    }
}
