/**
 * 23. Check whether all the characters in a given string are vowels
 * (a, e, i, o, u). Return true if every character is a vowel, otherwise
 * false. (The scan's sample input is OCR-garbled beyond reliable
 * reconstruction, so this demos with a clean all-vowel and a mixed string.)
 */
public class Q23AllCharsVowels {

    static boolean allVowels(String text) {
        for (char c : text.toLowerCase().toCharArray()) {
            if ("aeiou".indexOf(c) < 0) return false;
        }
        return !text.isEmpty();
    }

    public static void main(String[] args) {
        System.out.println("Input a string: AEIOU");
        System.out.println("Check all the characters of the said string are Vowels or not!");
        System.out.println(allVowels("AEIOU"));

        System.out.println("Input a string: AIEEK");
        System.out.println(allVowels("AIEEK"));
    }
}
