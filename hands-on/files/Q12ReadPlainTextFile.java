/**
 * 12. Read a plain text file (whole content as a single String).
 */
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Q12ReadPlainTextFile {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the file path: ");
        String path = scanner.nextLine();

        String content = Files.readString(Path.of(path));
        System.out.println(content);
    }
}
