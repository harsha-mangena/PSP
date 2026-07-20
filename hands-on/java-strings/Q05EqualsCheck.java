/**
 * 5. Check whether two String objects contain the same data.
 *
 * "Stephen Edwin King" equals "Walter Winchell"? false
 * "Stephen Edwin King" equals "Mike Royko"? false
 */
public class Q05EqualsCheck {

    public static void main(String[] args) {
        String name = "Stephen Edwin King";
        System.out.println("\"" + name + "\" equals \"Walter Winchell\"? "
                + name.equals("Walter Winchell"));
        System.out.println("\"" + name + "\" equals \"Mike Royko\"? "
                + name.equals("Mike Royko"));
    }
}
