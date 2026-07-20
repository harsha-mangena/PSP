/**
 * 11. Check whether a string is a valid password.
 *
 * Password rules:
 * 1. A password must have at least eight characters.
 * 2. A password consists of only letters and digits.
 * 3. A password must contain at least two digits.
 *
 * Test Data: "abcd1234" -> Password is valid: abcd1234
 */
public class Q11PasswordValidator {

    static boolean isValidPassword(String password) {
        if (password.length() < 8) return false;
        if (!password.matches("[A-Za-z0-9]+")) return false;

        int digitCount = 0;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) digitCount++;
        }
        return digitCount >= 2;
    }

    public static void main(String[] args) {
        String password = "abcd1234";
        if (isValidPassword(password)) {
            System.out.println("Password is valid: " + password);
        } else {
            System.out.println("1. A password must have at least eight characters.");
            System.out.println("2. A password consists of only letters and digits.");
            System.out.println("3. A password must contain at least two digits");
        }
    }
}
