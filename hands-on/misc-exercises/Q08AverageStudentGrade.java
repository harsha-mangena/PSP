/**
 * Given a List of Student objects with fields name and grade, find the
 * average grade of all students.
 *
 * Input: [{"Alice", 85}, {"Bob", 92}] -> Output: 88.5
 */
import java.util.List;

public class Q08AverageStudentGrade {

    record Student(String name, int grade) {}

    static double averageGrade(List<Student> students) {
        return students.stream().mapToInt(Student::grade).average().orElse(0);
    }

    public static void main(String[] args) {
        List<Student> students = List.of(new Student("Alice", 85), new Student("Bob", 92));
        System.out.println(averageGrade(students));
    }
}
