/**
 * Given a String representing a price (e.g. "$123.45"), extract the numeric
 * value as a double.
 *
 * Input: "$123.45" -> Output: 123.45
 */
import java.util.Scanner;

public class Q01ExtractPriceValue {

    static double extractPrice(String priceText) {
        String numeric = priceText.replaceAll("[^0-9.]", "");
        return Double.parseDouble(numeric);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input a price string (e.g. $123.45): ");
        String priceText = scanner.nextLine();

        System.out.println(extractPrice(priceText));
    }
}
