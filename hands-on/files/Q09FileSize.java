/**
 * 9. Get file size in bytes, KB and MB.
 */
import java.io.File;
import java.util.Scanner;

public class Q09FileSize {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the file path: ");
        String path = scanner.nextLine();

        File file = new File(path);
        long bytes = file.length();
        double kb = bytes / 1024.0;
        double mb = kb / 1024.0;

        System.out.println("File size in bytes: " + bytes);
        System.out.printf("File size in KB: %.4f%n", kb);
        System.out.printf("File size in MB: %.6f%n", mb);
    }
}
