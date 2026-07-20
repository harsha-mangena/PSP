/**
 * 7. Get the last modified time of a file.
 */
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Q07LastModifiedTime {

    public static void main(String[] args) {
        File file = new File("sample-data/sample.txt");
        long lastModified = file.lastModified();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(file.getPath() + " last modified: " + formatter.format(new Date(lastModified)));
    }
}
