/**
 * 3. Find sequences of lowercase letters joined with an underscore.
 */
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Q03LowercaseUnderscoreSequences {

    public static void main(String[] args) {
        String text = "hello_world and foo_bar_baz plus JavaCode and single";
        Pattern pattern = Pattern.compile("[a-z]+(_[a-z]+)+");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            System.out.println(matcher.group());
        }
    }
}
