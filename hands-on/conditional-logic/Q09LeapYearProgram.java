/**
 * 9. Take a year from the user and print whether that year is a leap year.
 *
 * Test Data: 2016 -> 2016 is a leap year
 */
public class Q09LeapYearProgram {

    static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    public static void main(String[] args) {
        int year = 2016;
        System.out.println(year + (isLeapYear(year) ? " is a leap year" : " is not a leap year"));
    }
}
