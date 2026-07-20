/**
 * 19. Accept three integers and return true if one of them is the midpoint
 * between the other two.
 *
 * Test Data: 2, 4, 6 -> true (4 is the midpoint of 2 and 6)
 */
public class Q19MidpointCheck {

    static boolean hasMidpoint(int a, int b, int c) {
        return a == (b + c) / 2.0 || b == (a + c) / 2.0 || c == (a + b) / 2.0;
    }

    public static void main(String[] args) {
        System.out.println("Check whether the three said numbers has a midpoint!");
        System.out.println(hasMidpoint(2, 4, 6));
    }
}
