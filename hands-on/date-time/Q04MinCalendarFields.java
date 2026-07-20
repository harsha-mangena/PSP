/**
 * 4. Get the minimum value of year, month, week, date from the current date
 * of a default calendar.
 */
import java.util.Calendar;

public class Q04MinCalendarFields {

    public static void main(String[] args) {
        Calendar cal = Calendar.getInstance();

        System.out.println("Minimum year: " + cal.getActualMinimum(Calendar.YEAR));
        System.out.println("Minimum month: " + cal.getActualMinimum(Calendar.MONTH));
        System.out.println("Minimum week of year: " + cal.getActualMinimum(Calendar.WEEK_OF_YEAR));
        System.out.println("Minimum date: " + cal.getActualMinimum(Calendar.DATE));
    }
}
