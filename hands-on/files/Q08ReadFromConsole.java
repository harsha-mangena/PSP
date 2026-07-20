/**
 * 8. Read input from the Java console.
 *
 * Run with input piped in, e.g.: echo "Hello" | java Q08ReadFromConsole
 */
import java.util.Scanner;

public class Q08ReadFromConsole {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter some text: ");
        if (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println("You entered: " + line);
        } else {
            System.out.println("No input provided.");
        }
    }
}
