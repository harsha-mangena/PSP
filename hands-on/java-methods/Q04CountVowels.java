/**
 * 4. Count all vowels in a string.
 *
 * Test Data: "w3resource" -> Number of Vowels in the string: 4
 */
public class Q04CountVowels {

    static int countVowels(String text) {
        int count = 0;
        for (char c : text.toLowerCase().toCharArray()) {
            if ("aeiou".indexOf(c) >= 0) count++;
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println("Number of Vowels in the string: " + countVowels("w3resource"));
    }
}
