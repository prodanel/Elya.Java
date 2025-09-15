public class Student {
    private String firstName;
    private String lastName;

    public Student(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public boolean compare(Student other) {
        return this.lastName.equals(other.getLastName());
    }

    public boolean compare(String lastName) {
        return this.lastName.equals(lastName);
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return "Студент{" +
                "firstName='" + firstName + '\'' +
        ", lastName='" + firstName + '\'' +
        '}';
    }

    public static void main(String[] args) {
        Student student1 = new Student("Иван", "Иванов");
        Student student2 = new Student("Сергей", "Иванов");
        System.out.println(student1.compare(student2));
        System.out.println(student1.compare("Иванов"));
        System.out.println(student1.compare("Петров"));
    }
}


