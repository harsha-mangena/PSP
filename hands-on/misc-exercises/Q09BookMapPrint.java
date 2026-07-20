/**
 * Given a Map storing book titles as keys and their authors as values, print
 * all book titles along with their authors.
 *
 * Input: {"The Hobbit": "J.R.R. Tolkien", "Pride and Prejudice": "Jane Austen"}
 * Output:
 * The Hobbit by J.R.R. Tolkien
 * Pride and Prejudice by Jane Austen
 */
import java.util.LinkedHashMap;
import java.util.Map;

public class Q09BookMapPrint {

    public static void main(String[] args) {
        Map<String, String> books = new LinkedHashMap<>();
        books.put("The Hobbit", "J.R.R. Tolkien");
        books.put("Pride and Prejudice", "Jane Austen");

        for (Map.Entry<String, String> entry : books.entrySet()) {
            System.out.println(entry.getKey() + " by " + entry.getValue());
        }
    }
}
