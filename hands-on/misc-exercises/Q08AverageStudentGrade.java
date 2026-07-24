/**
 * Given a list of Student records with fields name and grade, find the
 * average grade of all students.
 *
 * Input: Alice 85, Bob 92 -> Output: 88.5
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Q08AverageStudentGrade {

    record Student(String name, int grade) {}

    static double averageGrade(List<Student> students) {
        return students.stream().mapToInt(Student::grade).average().orElse(0);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input how many students: ");
        int count = scanner.nextInt();

        List<Student> students = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            System.out.print("Input the name for student " + (i + 1) + ": ");
            String name = scanner.next();
            System.out.print("Input the grade for student " + (i + 1) + ": ");
            int grade = scanner.nextInt();
            students.add(new Student(name, grade));
        }

        System.out.println(averageGrade(students));
    }
}
