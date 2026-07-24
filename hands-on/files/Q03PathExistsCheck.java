/**
 * 3. Check if a file or directory specified by pathname exists or not.
 */
import java.io.File;
import java.util.Scanner;

public class Q03PathExistsCheck {

    static boolean exists(String path) {
        return new File(path).exists();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the path to check: ");
        String path = scanner.nextLine();

        System.out.println(path + " exists? " + exists(path));
    }
}
