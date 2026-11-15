package StudentGradeManagement;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
class Student {
    private int id;
    private String name;
    private Map<String, Integer> grades;
    // Constructor
    public Student(int id, String name) {
        this.id = id;
        setName(name);
        grades = new LinkedHashMap<>();
    }
    // Getter for ID
    public int getId() {
        return id;
    }
    // Getter for name
    public String getName() {
        return name;
    }
    // Setter for name
    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        }
    }
    // Record marks
    public void addGrade(String course, int marks) {
        if (marks >= 0 && marks <= 100) {
            grades.put(course, marks);
        } else {
            System.out.println("Invalid marks! Enter 0 to 100.");
        }
    }
    // Get grades
    public Map<String, Integer> getGrades() {
        return new LinkedHashMap<>(grades);
    }
    // Calculate GPA
    public double calculateGPA() {
        if (grades.isEmpty()) {
            return 0.0;
        }
        double total = 0;
        for (int marks : grades.values()) {
            total += getGradePoint(marks);
        }
        return total / grades.size();
    }
    // Convert marks into grade points
    private double getGradePoint(int marks) {
        if (marks >= 90)
            return 4.0;
        else if (marks >= 85)
            return 3.7;
        else if (marks >= 80)
            return 3.3;
        else if (marks >= 75)
            return 3.0;
        else if (marks >= 70)
            return 2.7;
        else if (marks >= 65)
            return 2.3;
        else if (marks >= 60)
            return 2.0;
        else if (marks >= 50)
            return 1.0;
        else
            return 0.0;
    }
}
public class GradeManager {
    static ArrayList<Student> students = new ArrayList<>();
    static Scanner input = new Scanner(System.in);
    // Find student using ID
    public static Student findStudent(int id) {
        for (Student s : students) {
            if (s.getId() == id) {
                return s;
            }
        }
        return null;
    }
    // Add student
    public static void addStudent() {
        System.out.print("Enter Student ID: ");
        int id = readInt();
        if (id <= 0) {
            System.out.println("ID must be positive.");
            return;
        }
        if (findStudent(id) != null) {
            System.out.println("Student ID already exists!");
            return;
        }
        System.out.print("Enter Student Name: ");
        String name = input.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }
        Student s = new Student(id, name);
        students.add(s);
        System.out.println("Student added successfully!");
    }
    // Record marks
    public static void recordMarks() {
        System.out.print("Enter Student ID: ");
        int id = readInt();
        Student s = findStudent(id);
        if (s == null) {
            System.out.println("Student not found!");
            return;
        }
        System.out.print("Enter Course Name: ");
        String course = input.nextLine().trim();
        if (course.isEmpty()) {
            System.out.println("Course name cannot be empty.");
            return;
        }
        System.out.print("Enter Marks (0-100): ");
        int marks = readInt();
        if (marks < 0 || marks > 100) {
            System.out.println("Invalid marks!");
            return;
        }
        s.addGrade(course, marks);
        System.out.println("Marks recorded successfully!");
    }
    // Calculate class average and median
    public static void calculateClassStatistics() {
        ArrayList<Integer> allMarks = new ArrayList<>();
        for (Student s : students) {
            for (int marks : s.getGrades().values()) {
                allMarks.add(marks);
            }
        }
        if (allMarks.isEmpty()) {
            System.out.println("No marks available!");
            return;
        }
        double total = 0;
        for (int marks : allMarks) {
            total += marks;
        }
        double average = total / allMarks.size();
        Collections.sort(allMarks);
        int n = allMarks.size();
        double median;
        if (n % 2 == 0) {
            median = (allMarks.get(n / 2 - 1)
                    + allMarks.get(n / 2)) / 2.0;
        } else {
            median = allMarks.get(n / 2);
        }
        System.out.println("\n------ CLASS STATISTICS ------");
        System.out.printf("Total Marks Recorded: %d%n", n);
        System.out.printf("Class Average: %.2f%n", average);
        System.out.printf("Class Median: %.2f%n", median);
    }
    // Generate report cards
    public static void generateReportCards() {
        if (students.isEmpty()) {
            System.out.println("No students registered!");
            return;
        }
        System.out.println("\n================ STUDENT REPORT CARDS ================");
        for (Student s : students) {
            System.out.println("\nStudent ID: " + s.getId());
            System.out.println("Student Name: " + s.getName());
            System.out.println("------------------------------------------");
            System.out.printf("%-20s %-10s%n", "Course", "Marks");
            System.out.println("------------------------------------------");
            Map<String, Integer> grades = s.getGrades();
            if (grades.isEmpty()) {
                System.out.println("No marks recorded yet.");
            } else {
                for (Map.Entry<String, Integer> entry : grades.entrySet()) {
                    System.out.printf("%-20s %-10d%n",
                            entry.getKey(), entry.getValue());
                }
            }
            System.out.println("------------------------------------------");
            System.out.printf("Cumulative GPA: %.2f / 4.00%n",
                    s.calculateGPA());

        }
        System.out.println("\n======================================================");
    }
    // Read integer safely
    public static int readInt() {
        while (true) {
            try {
                int number = Integer.parseInt(input.nextLine());
                return number;
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }
    // Main method
    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("\n=================================");
            System.out.println("   STUDENT GRADE MANAGEMENT");
            System.out.println("=================================");
            System.out.println("1. Add Student");
            System.out.println("2. Record Marks");
            System.out.println("3. Compute Class Average & Median");
            System.out.println("4. Generate Report Cards");
            System.out.println("5. Exit");
            System.out.println("=================================");
            System.out.print("Enter your choice: ");
            choice = readInt();
            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    recordMarks();
                    break;
                case 3:
                    calculateClassStatistics();
                    break;
                case 4:
                    generateReportCards();
                    break;
                case 5:
                    System.out.println("Thank you for using the system!");
                    break;
                default:
                    System.out.println("Invalid choice! Try again.");
            }

        } while (choice != 5);

        input.close();
    }
}
