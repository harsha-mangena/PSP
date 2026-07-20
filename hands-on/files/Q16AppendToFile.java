/**
 * 16. Append text to an existing file.
 */
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Q16AppendToFile {

    public static void main(String[] args) throws IOException {
        Path path = Path.of("sample-data/output.txt");
        if (!Files.exists(path)) {
            Files.writeString(path, "Initial line." + System.lineSeparator());
        }

        try (FileWriter writer = new FileWriter(path.toFile(), true)) {
            writer.write("Appended line." + System.lineSeparator());
        }

        System.out.println("File content after append:");
        System.out.println(Files.readString(path));
    }
}
