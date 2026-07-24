/**
 * 10. Read contents from a file into a byte array.
 */
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Q10ReadFileIntoByteArray {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the file path: ");
        String path = scanner.nextLine();

        byte[] bytes = Files.readAllBytes(Path.of(path));
        System.out.println("Read " + bytes.length + " bytes.");
        System.out.println("As text: " + new String(bytes));
    }
}
