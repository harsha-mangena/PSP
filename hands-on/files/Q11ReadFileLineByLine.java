/**
 * 11. Read a file's content line by line.
 */
import java.io.BufferedReader;
import java.io.FileReader;

public class Q11ReadFileLineByLine {

    public static void main(String[] args) throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("sample-data/sample.txt"))) {
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                System.out.println(lineNumber + ": " + line);
                lineNumber++;
            }
        }
    }
}
