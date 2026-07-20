/**
 * 6. Compare a given string to another string, ignoring case considerations.
 *
 * "Stephen Edwin King" equals "Walter Winchell"? false
 * "Stephen Edwin King" equals "stephen edwin king"? true
 */
public class Q06EqualsIgnoreCase {

    public static void main(String[] args) {
        String name = "Stephen Edwin King";
        System.out.println("\"" + name + "\" equals \"Walter Winchell\"? "
                + name.equalsIgnoreCase("Walter Winchell"));
        System.out.println("\"" + name + "\" equals \"stephen edwin king\"? "
                + name.equalsIgnoreCase("stephen edwin king"));
    }
}
