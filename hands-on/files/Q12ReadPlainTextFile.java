/**
 * 12. Read a plain text file (whole content as a single String).
 */
import java.nio.file.Files;
import java.nio.file.Path;

public class Q12ReadPlainTextFile {

    public static void main(String[] args) throws Exception {
        String content = Files.readString(Path.of("sample-data/sample.txt"));
        System.out.println(content);
    }
}
