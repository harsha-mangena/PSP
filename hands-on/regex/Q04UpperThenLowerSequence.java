/**
 * 4. Find the sequences of one uppercase letter followed by lowercase
 * letters.
 */
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Q04UpperThenLowerSequence {

    public static void main(String[] args) {
        String text = "Hello World, this is a Test of PascalCase words";
        Pattern pattern = Pattern.compile("[A-Z][a-z]+");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            System.out.println(matcher.group());
        }
    }
}
