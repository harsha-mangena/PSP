/**
 * 1. Get a list of all file/directory names from a given directory.
 */
import java.io.File;

public class Q01ListFilesInDirectory {

    public static void main(String[] args) {
        File dir = new File("sample-data");
        String[] names = dir.list();

        if (names == null) {
            System.out.println("Not a directory or an I/O error occurred.");
            return;
        }

        java.util.Arrays.sort(names);
        for (String name : names) {
            System.out.println(name);
        }
    }
}
