/**
 * 1. Get a list of all file/directory names from a given directory.
 */
import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class Q01ListFilesInDirectory {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the directory path (e.g. sample-data): ");
        String path = scanner.nextLine();

        File dir = new File(path);
        String[] names = dir.list();

        if (names == null) {
            System.out.println("Not a directory or an I/O error occurred.");
            return;
        }

        Arrays.sort(names);
        for (String name : names) {
            System.out.println(name);
        }
    }
}
