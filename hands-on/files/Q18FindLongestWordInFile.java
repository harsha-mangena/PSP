/**
 * 18. Find the longest word in a text file.
 *
 * sample.txt contains the word "extraordinarily" which should come back as
 * the longest word once punctuation is stripped.
 */
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Q18FindLongestWordInFile {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the file path: ");
        String path = scanner.nextLine();

        String content = Files.readString(Path.of(path));
        String[] words = content.split("[^A-Za-z']+");

        String longest = "";
        for (String word : words) {
            if (word.length() > longest.length()) {
                longest = word;
            }
        }

        System.out.println("Longest word: " + longest + " (" + longest.length() + " characters)");
    }
}
