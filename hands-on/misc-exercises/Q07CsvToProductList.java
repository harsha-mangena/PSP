/**
 * Read a CSV file where each line contains product data
 * (e.g. "123,Laptop,999.99"). Store each line's data in a Product object and
 * add the objects to a List. Then print the product information.
 *
 * Input (CSV file content):
 * 123,Laptop,999.99
 * 456,Phone,599.99
 *
 * Output:
 * Product ID: 123, Name: Laptop, Price: 999.99
 * Product ID: 456, Name: Phone, Price: 599.99
 */
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Q07CsvToProductList {

    static class Product {
        final String productId;
        final String productName;
        final double price;

        Product(String productId, String productName, double price) {
            this.productId = productId;
            this.productName = productName;
            this.price = price;
        }

        @Override
        public String toString() {
            return "Product ID: " + productId + ", Name: " + productName + ", Price: " + price;
        }
    }

    static List<Product> readProducts(Path csvPath) throws Exception {
        List<Product> products = new ArrayList<>();
        for (String line : Files.readAllLines(csvPath)) {
            if (line.isBlank()) continue;
            String[] parts = line.split(",");
            products.add(new Product(parts[0], parts[1], Double.parseDouble(parts[2])));
        }
        return products;
    }

    public static void main(String[] args) throws Exception {
        List<Product> products = readProducts(Path.of("sample-data/products.csv"));
        for (Product product : products) {
            System.out.println(product);
        }
    }
}
