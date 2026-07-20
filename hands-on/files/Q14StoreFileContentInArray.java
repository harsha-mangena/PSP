/**
 * 14. Store text file content, line by line, into an array.
 */
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Q14StoreFileContentInArray {

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Path.of("sample-data/sample.txt"));
        String[] array = lines.toArray(new String[0]);

        System.out.println("Line count: " + array.length);
        for (int i = 0; i < array.length; i++) {
            System.out.println("[" + i + "] " + array[i]);
        }
    }
}
