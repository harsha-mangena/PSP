/**
 * 7. Find the number of days in a month.
 *
 * Test Data: month 2, year 2016 -> February 2016 has 29 days
 */
public class Q07DaysInMonth {

    static final String[] MONTH_NAMES = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    };

    static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    static int daysInMonth(int month, int year) {
        int[] days = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (month == 2 && isLeapYear(year)) return 29;
        return days[month - 1];
    }

    public static void main(String[] args) {
        int month = 2, year = 2016;
        System.out.println(MONTH_NAMES[month - 1] + " " + year + " has "
                + daysInMonth(month, year) + " days");
    }
}
