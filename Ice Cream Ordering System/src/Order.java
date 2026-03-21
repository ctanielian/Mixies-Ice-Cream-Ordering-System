public class Order {
    private int orderID;
    private double subtotal;
    private double tip;
    private double total;

    // Default constructor with subtotal and tip
    public Order(double subtotal, double tip) {
        this.subtotal = subtotal;
        this.tip = tip;
    }

    // Getter and Setter Methods
    public int getID() {
        return orderID;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getTip() {
        return tip;
    }

    public double getTotal() {
        return total;
    }
}
