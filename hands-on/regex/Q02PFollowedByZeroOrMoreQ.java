/**
 * 2. Match a string that has a 'p' followed by zero or more 'q's.
 */
public class Q02PFollowedByZeroOrMoreQ {

    static boolean isPFollowedByZeroOrMoreQ(String text) {
        return text.matches("pq*");
    }

    public static void main(String[] args) {
        System.out.println(isPFollowedByZeroOrMoreQ("p"));      // true - zero q's
        System.out.println(isPFollowedByZeroOrMoreQ("pq"));     // true
        System.out.println(isPFollowedByZeroOrMoreQ("pqqqq"));  // true
        System.out.println(isPFollowedByZeroOrMoreQ("pqr"));    // false - trailing r
        System.out.println(isPFollowedByZeroOrMoreQ("qp"));     // false - wrong order
    }
}
