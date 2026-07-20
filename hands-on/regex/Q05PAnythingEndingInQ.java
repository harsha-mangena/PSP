/**
 * 5. Match a string that has a 'p', followed by anything, ending in 'q'.
 */
public class Q05PAnythingEndingInQ {

    static boolean matches(String text) {
        return text.matches("p.*q");
    }

    public static void main(String[] args) {
        System.out.println(matches("pq"));        // true
        System.out.println(matches("pxyzq"));      // true
        System.out.println(matches("pqpq"));       // true
        System.out.println(matches("pxyzr"));      // false - doesn't end in q
        System.out.println(matches("xpyzq"));      // false - doesn't start with p
    }
}
