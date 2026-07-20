/**
 * 2. Get specific files by extension from a specified folder.
 *
 * Test Data: folder "sample-data", extension ".txt"
 */
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

public class Q02FilesByExtension {

    static String[] filesByExtension(String folder, String extension) {
        File dir = new File(folder);
        FilenameFilter filter = (d, name) -> name.toLowerCase().endsWith(extension.toLowerCase());
        String[] names = dir.list(filter);
        return names == null ? new String[0] : names;
    }

    public static void main(String[] args) {
        String[] result = filesByExtension("sample-data", ".txt");
        Arrays.sort(result);
        System.out.println(".txt files in sample-data:");
        for (String name : result) {
            System.out.println(name);
        }
    }
}
