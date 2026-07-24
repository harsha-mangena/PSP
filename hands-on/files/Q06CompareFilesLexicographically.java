/**
 * 6. Compare two files lexicographically (by their content).
 */
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Q06CompareFilesLexicographically {

    static int compareFiles(String path1, String path2) throws Exception {
        String content1 = Files.readString(Path.of(path1));
        String content2 = Files.readString(Path.of(path2));
        return content1.compareTo(content2);
    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the first file path: ");
        String path1 = scanner.nextLine();
        System.out.print("Input the second file path: ");
        String path2 = scanner.nextLine();

        int result = compareFiles(path1, path2);
        System.out.println("Comparison result: " + result);
        System.out.println(result == 0 ? "Files are identical"
                : (result < 0 ? path1 + " comes before " + path2 : path1 + " comes after " + path2));
    }
}
