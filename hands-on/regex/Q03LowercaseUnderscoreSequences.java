/**
 * 3. Find sequences of lowercase letters joined with an underscore.
 */
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Q03LowercaseUnderscoreSequences {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input a string: ");
        String text = scanner.nextLine();

        Pattern pattern = Pattern.compile("[a-z]+(_[a-z]+)+");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            System.out.println(matcher.group());
        }
    }
}
