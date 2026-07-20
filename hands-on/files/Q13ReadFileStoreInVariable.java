/**
 * 13. Read a file line by line and store it into a variable.
 */
import java.io.BufferedReader;
import java.io.FileReader;

public class Q13ReadFileStoreInVariable {

    public static void main(String[] args) throws Exception {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("sample-data/sample.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        }
        System.out.println("Stored content:");
        System.out.println(content);
    }
}
