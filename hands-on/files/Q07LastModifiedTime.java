/**
 * 7. Get the last modified time of a file.
 */
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Q07LastModifiedTime {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the file path: ");
        String path = scanner.nextLine();

        File file = new File(path);
        long lastModified = file.lastModified();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(file.getPath() + " last modified: " + formatter.format(new Date(lastModified)));
    }
}
