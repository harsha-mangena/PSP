/**
 * 9. Take a year from the user and print whether that year is a leap year.
 *
 * Test Data: 2016 -> 2016 is a leap year
 */
import java.util.Scanner;

public class Q09LeapYearProgram {

    static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the year: ");
        int year = scanner.nextInt();

        System.out.println(year + (isLeapYear(year) ? " is a leap year" : " is not a leap year"));
    }
}
