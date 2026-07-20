/**
 * 1. Create a Date object using the Calendar class.
 */
import java.util.Calendar;

public class Q01CreateDateWithCalendar {

    public static void main(String[] args) {
        int year = 2016;
        int month = 0; // January (Calendar months are 0-based)
        int date = 1;

        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, date);

        System.out.println(cal.getTime());
    }
}
