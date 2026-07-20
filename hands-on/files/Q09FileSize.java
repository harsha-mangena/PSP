/**
 * 9. Get file size in bytes, KB and MB.
 */
import java.io.File;

public class Q09FileSize {

    public static void main(String[] args) {
        File file = new File("sample-data/sample.txt");
        long bytes = file.length();
        double kb = bytes / 1024.0;
        double mb = kb / 1024.0;

        System.out.println("File size in bytes: " + bytes);
        System.out.printf("File size in KB: %.4f%n", kb);
        System.out.printf("File size in MB: %.6f%n", mb);
    }
}
