public class Customer {

    // Customer information
    private int customerID;
    private String name;
    private String phoneNumber;

    // Default constructor requiring name and phone number
    public Customer(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    // Getter and Setter Methods
    public int getID() {
        return customerID;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setName(String newName) {
        name = newName;
    }

    public void setPhoneNumber(String newPhoneNumber) {
        phoneNumber = newPhoneNumber;
    }
}
