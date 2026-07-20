/**
 * 17. Read the first N lines from a file.
 *
 * Test Data: first 3 lines of sample.txt
 */
import java.io.BufferedReader;
import java.io.FileReader;

public class Q17ReadFirstNLines {

    public static void main(String[] args) throws Exception {
        int n = 3;
        try (BufferedReader reader = new BufferedReader(new FileReader("sample-data/sample.txt"))) {
            String line;
            int count = 0;
            while (count < n && (line = reader.readLine()) != null) {
                System.out.println(line);
                count++;
            }
        }
    }
}
