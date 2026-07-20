/**
 * 7. Get the last day of the current month.
 */
import java.time.YearMonth;

public class Q07LastDayOfCurrentMonth {

    public static void main(String[] args) {
        System.out.println("Last day of the current month: "
                + YearMonth.now().atEndOfMonth());
    }
}
