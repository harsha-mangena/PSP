/**
 * 3. Check if a file or directory specified by pathname exists or not.
 */
import java.io.File;

public class Q03PathExistsCheck {

    static boolean exists(String path) {
        return new File(path).exists();
    }

    public static void main(String[] args) {
        System.out.println("sample-data/sample.txt exists? " + exists("sample-data/sample.txt"));
        System.out.println("sample-data/does-not-exist.txt exists? "
                + exists("sample-data/does-not-exist.txt"));
    }
}
