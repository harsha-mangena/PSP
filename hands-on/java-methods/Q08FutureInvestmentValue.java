/**
 * 8. Compute the future investment value at a given interest rate for a
 * specified number of years (monthly compounded).
 *
 * FV = amount * (1 + rate/1200)^(12 * years)
 *
 * Test Data: amount 1000, rate 10%, years 5
 * Expected Output:
 * Years  FutureValue
 * 1      1104.71
 * 2      1220.39
 * 3      1348.18
 * 4      1489.35
 * 5      1645.31
 */
import java.util.Scanner;

public class Q08FutureInvestmentValue {

    static double futureValue(double amount, double annualRatePercent, int years) {
        double monthlyRate = annualRatePercent / 1200.0;
        return amount * Math.pow(1 + monthlyRate, 12.0 * years);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the investment amount: ");
        double amount = scanner.nextDouble();
        System.out.print("Input the rate of interest: ");
        double rate = scanner.nextDouble();
        System.out.print("Input number of years: ");
        int years = scanner.nextInt();

        System.out.println("Years\tFutureValue");
        for (int y = 1; y <= years; y++) {
            System.out.printf("%d\t%.2f%n", y, futureValue(amount, rate, y));
        }
    }
}
