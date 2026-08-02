import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Student Grade Tracker
 * -----------------------------------------------------
 * Console-based program that:
 *  - Inputs student names and their scores (multiple subjects allowed)
 *  - Stores everything in an ArrayList<Student>
 *  - Calculates each student's average
 *  - Calculates the highest and lowest scoring student (by average)
 *  - Displays a full summary report
 */
public class StudentGradeTracker {

    // ---------- Student model ----------
    static class Student {
        String name;
        List<Double> scores = new ArrayList<>();

        Student(String name) {
            this.name = name;
        }

        void addScore(double score) {
            scores.add(score);
        }

        double average() {
            if (scores.isEmpty()) return 0.0;
            double sum = 0;
            for (double s : scores) sum += s;
            return sum / scores.size();
        }

        double highestScore() {
            return scores.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
        }

        double lowestScore() {
            return scores.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
        }
    }

    // ---------- Main program ----------
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Student> students = new ArrayList<>();

        System.out.println("=== Student Grade Tracker ===");

        boolean running = true;
        while (running) {
            System.out.println("\nMenu:");
            System.out.println("1. Add a student");
            System.out.println("2. Add a score to a student");
            System.out.println("3. View summary report");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    addStudent(sc, students);
                    break;
                case "2":
                    addScoreToStudent(sc, students);
                    break;
                case "3":
                    printSummaryReport(students);
                    break;
                case "4":
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1-4.");
            }
        }

        sc.close();
    }

    private static void addStudent(Scanner sc, List<Student> students) {
        System.out.print("Enter student name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }

        Student student = new Student(name);

        System.out.print("How many scores would you like to enter now? ");
        int count = readInt(sc);

        for (int i = 0; i < count; i++) {
            System.out.print("  Enter score " + (i + 1) + " for " + name + ": ");
            double score = readDouble(sc);
            student.addScore(score);
        }

        students.add(student);
        System.out.println(name + " added with " + count + " score(s).");
    }

    private static void addScoreToStudent(Scanner sc, List<Student> students) {
        if (students.isEmpty()) {
            System.out.println("No students yet. Add a student first.");
            return;
        }

        System.out.print("Enter student name to add a score to: ");
        String name = sc.nextLine().trim();

        Student found = findStudent(students, name);
        if (found == null) {
            System.out.println("Student not found: " + name);
            return;
        }

        System.out.print("Enter score to add: ");
        double score = readDouble(sc);
        found.addScore(score);
        System.out.println("Score added to " + found.name + ".");
    }

    private static Student findStudent(List<Student> students, String name) {
        for (Student s : students) {
            if (s.name.equalsIgnoreCase(name)) return s;
        }
        return null;
    }

    private static void printSummaryReport(List<Student> students) {
        if (students.isEmpty()) {
            System.out.println("No students to report on yet.");
            return;
        }

        System.out.println("\n==================== SUMMARY REPORT ====================");
        System.out.printf("%-15s %-10s %-10s %-10s %-10s%n",
                "Name", "Scores#", "Average", "Highest", "Lowest");
        System.out.println("----------------------------------------------------------");

        Student topStudent = null;
        Student bottomStudent = null;

        for (Student s : students) {
            if (s.scores.isEmpty()) {
                System.out.printf("%-15s %-10s %-10s %-10s %-10s%n",
                        s.name, 0, "N/A", "N/A", "N/A");
                continue;
            }

            System.out.printf("%-15s %-10d %-10.2f %-10.2f %-10.2f%n",
                    s.name, s.scores.size(), s.average(), s.highestScore(), s.lowestScore());

            if (topStudent == null || s.average() > topStudent.average()) topStudent = s;
            if (bottomStudent == null || s.average() < bottomStudent.average()) bottomStudent = s;
        }

        System.out.println("----------------------------------------------------------");

        if (topStudent != null) {
            System.out.printf("Highest class average: %s (%.2f)%n", topStudent.name, topStudent.average());
            System.out.printf("Lowest class average:  %s (%.2f)%n", bottomStudent.name, bottomStudent.average());

            double classSum = 0;
            int counted = 0;
            for (Student s : students) {
                if (!s.scores.isEmpty()) {
                    classSum += s.average();
                    counted++;
                }
            }
            double classAverage = counted > 0 ? classSum / counted : 0;
            System.out.printf("Overall class average:  %.2f%n", classAverage);
        }

        System.out.println("==========================================================");
    }

    // ---------- Input helpers with basic validation ----------
    private static int readInt(Scanner sc) {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid whole number: ");
            }
        }
    }

    private static double readDouble(Scanner sc) {
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }
}
