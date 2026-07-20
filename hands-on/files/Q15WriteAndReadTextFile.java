/**
 * 15. Write and read a plain text file.
 */
import java.nio.file.Files;
import java.nio.file.Path;

public class Q15WriteAndReadTextFile {

    public static void main(String[] args) throws Exception {
        Path path = Path.of("sample-data/output.txt");
        String content = "Hello, this file was written by Q15WriteAndReadTextFile.";

        Files.writeString(path, content);
        System.out.println("Wrote: " + content);

        String readBack = Files.readString(path);
        System.out.println("Read back: " + readBack);
    }
}
