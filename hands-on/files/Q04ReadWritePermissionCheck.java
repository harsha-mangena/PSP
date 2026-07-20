/**
 * 4. Check if a file or directory has read and write permission.
 */
import java.io.File;

public class Q04ReadWritePermissionCheck {

    public static void main(String[] args) {
        File file = new File("sample-data/sample.txt");
        System.out.println(file.getPath() + " readable? " + file.canRead());
        System.out.println(file.getPath() + " writable? " + file.canWrite());
    }
}
