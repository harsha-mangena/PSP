/**
 * 2. Get and display information (year, month, day, hour, minute) of a
 * default calendar.
 */
import java.util.Calendar;

public class Q02DisplayCalendarInfo {

    public static void main(String[] args) {
        Calendar cal = Calendar.getInstance();

        System.out.println("Year: " + cal.get(Calendar.YEAR));
        System.out.println("Month: " + (cal.get(Calendar.MONTH) + 1));
        System.out.println("Day: " + cal.get(Calendar.DAY_OF_MONTH));
        System.out.println("Hour: " + cal.get(Calendar.HOUR_OF_DAY));
        System.out.println("Minute: " + cal.get(Calendar.MINUTE));
    }
}
