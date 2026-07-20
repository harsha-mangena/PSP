/**
 * 10. Check whether a year (integer) is a leap year or not.
 *
 * Test Data: 2017 -> false
 */
public class Q10LeapYearCheck {

    static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    public static void main(String[] args) {
        System.out.println("Input a year: 2017");
        System.out.println(isLeapYear(2017));
    }
}
