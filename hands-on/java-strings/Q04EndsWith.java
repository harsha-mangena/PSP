/**
 * 4. Check whether a given string ends with the contents of another string.
 *
 * "Python Exercises" ends with "se"? false
 * "Python Exercise" ends with "se"? true
 */
public class Q04EndsWith {

    public static void main(String[] args) {
        System.out.println("\"Python Exercises\" ends with \"se\"? "
                + "Python Exercises".endsWith("se"));
        System.out.println("\"Python Exercise\" ends with \"se\"? "
                + "Python Exercise".endsWith("se"));
    }
}
