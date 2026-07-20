/**
 * 21. Display the factors of 3 in a given integer.
 *
 * Test Data: 81 -> 81 = 3 * 3 * 3 * 3 * 1
 */
public class Q21FactorsOfThree {

    static String factorsOfThree(int n) {
        StringBuilder sb = new StringBuilder(n + " = ");
        while (n % 3 == 0) {
            sb.append("3 * ");
            n /= 3;
        }
        sb.append(n);
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("Input an integer (positive/negative): 81");
        System.out.println("Factors of 3 of the said integer:");
        System.out.println(factorsOfThree(81));
    }
}
