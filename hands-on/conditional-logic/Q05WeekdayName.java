/**
 * 5. Take a number from the user (1-7) and display the name of the weekday.
 *
 * Test Data: 3 -> Wednesday
 */
import java.util.Scanner;

public class Q05WeekdayName {

    static String weekdayName(int number) {
        return switch (number) {
            case 1 -> "Monday";
            case 2 -> "Tuesday";
            case 3 -> "Wednesday";
            case 4 -> "Thursday";
            case 5 -> "Friday";
            case 6 -> "Saturday";
            case 7 -> "Sunday";
            default -> "Invalid day number";
        };
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input number: ");
        int number = scanner.nextInt();

        System.out.println(weekdayName(number));
    }
}
