/**
 * Given a map storing book titles as keys and their authors as values, print
 * all book titles along with their authors.
 *
 * Input: 2 books - "The Hobbit"/"J.R.R. Tolkien", "Pride and Prejudice"/"Jane Austen"
 * Output:
 * The Hobbit by J.R.R. Tolkien
 * Pride and Prejudice by Jane Austen
 */
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Q09BookMapPrint {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input how many books: ");
        int count = Integer.parseInt(scanner.nextLine().trim());

        Map<String, String> books = new LinkedHashMap<>();
        for (int i = 0; i < count; i++) {
            System.out.print("Input the title for book " + (i + 1) + ": ");
            String title = scanner.nextLine();
            System.out.print("Input the author for book " + (i + 1) + ": ");
            String author = scanner.nextLine();
            books.put(title, author);
        }

        for (Map.Entry<String, String> entry : books.entrySet()) {
            System.out.println(entry.getKey() + " by " + entry.getValue());
        }
    }
}
