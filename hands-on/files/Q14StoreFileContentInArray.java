/**
 * 14. Store text file content, line by line, into an array.
 */
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Q14StoreFileContentInArray {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the file path: ");
        String path = scanner.nextLine();

        List<String> lines = Files.readAllLines(Path.of(path));
        String[] array = lines.toArray(new String[0]);

        System.out.println("Line count: " + array.length);
        for (int i = 0; i < array.length; i++) {
            System.out.println("[" + i + "] " + array[i]);
        }
    }
}
