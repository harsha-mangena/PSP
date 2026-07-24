/**
 * 4. Check if a file or directory has read and write permission.
 */
import java.io.File;
import java.util.Scanner;

public class Q04ReadWritePermissionCheck {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the path to check: ");
        String path = scanner.nextLine();

        File file = new File(path);
        System.out.println(file.getPath() + " readable? " + file.canRead());
        System.out.println(file.getPath() + " writable? " + file.canWrite());
    }
}
