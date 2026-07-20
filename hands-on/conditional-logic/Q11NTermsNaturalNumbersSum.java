/**
 * 11. Display n terms of natural numbers and their sum.
 *
 * (The scanned sheet's sample output for this item is corrupted by the OCR
 * pass, so this demos with n = 5, consistent with the neighbouring exercises
 * that use the same input.)
 */
public class Q11NTermsNaturalNumbersSum {

    public static void main(String[] args) {
        int n = 5;
        int sum = 0;

        System.out.println("The first " + n + " natural numbers are:");
        for (int i = 1; i <= n; i++) {
            System.out.println(i);
            sum += i;
        }
        System.out.println("The Sum of Natural Number upto " + n + " terms: " + sum);
    }
}
