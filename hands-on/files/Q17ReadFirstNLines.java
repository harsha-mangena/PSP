/**
 * 17. Read the first N lines from a file.
 *
 * Test Data: first 3 lines of sample.txt
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class Q17ReadFirstNLines {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the file path: ");
        String path = scanner.nextLine();
        System.out.print("Input the number of lines: ");
        int n = scanner.nextInt();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            int count = 0;
            while (count < n && (line = reader.readLine()) != null) {
                System.out.println(line);
                count++;
            }
        }
    }
}
