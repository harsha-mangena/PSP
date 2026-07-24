/**
 * 15. Write and read a plain text file.
 */
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Q15WriteAndReadTextFile {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the file path to write: ");
        String path = scanner.nextLine();
        System.out.print("Input the text to write: ");
        String content = scanner.nextLine();

        Files.writeString(Path.of(path), content);
        System.out.println("Wrote: " + content);

        String readBack = Files.readString(Path.of(path));
        System.out.println("Read back: " + readBack);
    }
}
