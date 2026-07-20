/**
 * 5. Check if a given pathname is a directory or a file.
 */
import java.io.File;

public class Q05IsDirectoryOrFile {

    static String describe(String path) {
        File f = new File(path);
        if (!f.exists()) return path + " does not exist";
        return path + " is a " + (f.isDirectory() ? "directory" : "file");
    }

    public static void main(String[] args) {
        System.out.println(describe("sample-data"));
        System.out.println(describe("sample-data/sample.txt"));
    }
}
