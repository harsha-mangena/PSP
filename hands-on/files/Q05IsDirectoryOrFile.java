/**
 * 5. Check if a given pathname is a directory or a file.
 */
import java.io.File;
import java.util.Scanner;

public class Q05IsDirectoryOrFile {

    static String describe(String path) {
        File f = new File(path);
        if (!f.exists()) return path + " does not exist";
        return path + " is a " + (f.isDirectory() ? "directory" : "file");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the path to check: ");
        String path = scanner.nextLine();

        System.out.println(describe(path));
    }
}
