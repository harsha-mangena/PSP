/**
 * Take a birthdate (e.g. "1990-05-15") and return the person's age as an int.
 *
 * Input: "1990-05-15", assuming "today" is 2024 -> Output: 34
 *
 * The real-world method should compare against today's actual date, so it
 * takes the reference date as a parameter - that also makes it testable
 * against the exercise's fixed "assuming the current year is 2024" example.
 */
import java.time.LocalDate;
import java.time.Period;

public class Q02AgeFromBirthdate {

    static int age(String birthdate, LocalDate today) {
        LocalDate birth = LocalDate.parse(birthdate);
        return Period.between(birth, today).getYears();
    }

    static int age(String birthdate) {
        return age(birthdate, LocalDate.now());
    }

    public static void main(String[] args) {
        System.out.println(age("1990-05-15", LocalDate.of(2024, 6, 1)));
        System.out.println("Age as of today: " + age("1990-05-15"));
    }
}
