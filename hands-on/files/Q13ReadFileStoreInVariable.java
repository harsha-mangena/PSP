/**
 * 13. Read a file line by line and store it into a variable.
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class Q13ReadFileStoreInVariable {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the file path: ");
        String path = scanner.nextLine();

        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        }
        System.out.println("Stored content:");
        System.out.println(content);
    }
}
