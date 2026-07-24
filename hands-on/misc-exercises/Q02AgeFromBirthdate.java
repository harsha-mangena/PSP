/**
 * Take a birthdate (e.g. "1990-05-15") and return the person's age as an int.
 *
 * Input: "1990-05-15" -> Output: age in whole years as of today.
 */
import java.time.LocalDate;
import java.time.Period;
import java.util.Scanner;

public class Q02AgeFromBirthdate {

    static int age(String birthdate, LocalDate today) {
        LocalDate birth = LocalDate.parse(birthdate);
        return Period.between(birth, today).getYears();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the birthdate (yyyy-mm-dd): ");
        String birthdate = scanner.nextLine();

        System.out.println("Age as of today: " + age(birthdate, LocalDate.now()));
    }
}
