/**
 * 16. Append text to an existing file.
 */
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Q16AppendToFile {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the file path: ");
        String path = scanner.nextLine();
        System.out.print("Input the text to append: ");
        String textToAppend = scanner.nextLine();

        if (!Files.exists(Path.of(path))) {
            Files.writeString(Path.of(path), "Initial line." + System.lineSeparator());
        }

        try (FileWriter writer = new FileWriter(path, true)) {
            writer.write(textToAppend + System.lineSeparator());
        }

        System.out.println("File content after append:");
        System.out.println(Files.readString(Path.of(path)));
    }
}
