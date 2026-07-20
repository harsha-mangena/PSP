/**
 * 10. Read contents from a file into a byte array.
 */
import java.nio.file.Files;
import java.nio.file.Path;

public class Q10ReadFileIntoByteArray {

    public static void main(String[] args) throws Exception {
        byte[] bytes = Files.readAllBytes(Path.of("sample-data/sample2.txt"));
        System.out.println("Read " + bytes.length + " bytes.");
        System.out.println("As text: " + new String(bytes));
    }
}
