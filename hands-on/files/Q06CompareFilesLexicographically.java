/**
 * 6. Compare two files lexicographically (by their content).
 */
import java.nio.file.Files;
import java.nio.file.Path;

public class Q06CompareFilesLexicographically {

    static int compareFiles(String path1, String path2) throws Exception {
        String content1 = Files.readString(Path.of(path1));
        String content2 = Files.readString(Path.of(path2));
        return content1.compareTo(content2);
    }

    public static void main(String[] args) throws Exception {
        int result = compareFiles("sample-data/sample.txt", "sample-data/sample2.txt");
        System.out.println("Comparison result: " + result);
        System.out.println(result == 0 ? "Files are identical"
                : (result < 0 ? "sample.txt comes before sample2.txt" : "sample.txt comes after sample2.txt"));
    }
}
