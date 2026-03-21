public class Employee {
    private int empolyeeID;
    private String name;

    // Default constructor requiring name and phone number
    public Employee(String name) {
        this.name = name;
    }

    // Getter and Setter Methods
    public int getID() {
        return empolyeeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }
}
