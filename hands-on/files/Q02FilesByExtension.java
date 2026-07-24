/**
 * 2. Get specific files by extension from a specified folder.
 *
 * Test Data: folder "sample-data", extension ".txt"
 */
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Scanner;

public class Q02FilesByExtension {

    static String[] filesByExtension(String folder, String extension) {
        File dir = new File(folder);
        FilenameFilter filter = (d, name) -> name.toLowerCase().endsWith(extension.toLowerCase());
        String[] names = dir.list(filter);
        return names == null ? new String[0] : names;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the folder path: ");
        String folder = scanner.nextLine();
        System.out.print("Input the extension (e.g. .txt): ");
        String extension = scanner.nextLine();

        String[] result = filesByExtension(folder, extension);
        Arrays.sort(result);
        System.out.println(extension + " files in " + folder + ":");
        for (String name : result) {
            System.out.println(name);
        }
    }
}
