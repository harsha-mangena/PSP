/**
 * 11. Read a file's content line by line.
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class Q11ReadFileLineByLine {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the file path: ");
        String path = scanner.nextLine();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                System.out.println(lineNumber + ": " + line);
                lineNumber++;
            }
        }
    }
}
