/**
 * 13. Calculate the area of a triangle (Heron's formula).
 *
 * Test Data: sides 10, 15, 20 -> The area of the triangle is 72.6184377413890
 */
public class Q13TriangleArea {

    static double triangleArea(double a, double b, double c) {
        double s = (a + b + c) / 2;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }

    public static void main(String[] args) {
        System.out.println("The area of the triangle is " + triangleArea(10, 15, 20));
    }
}
