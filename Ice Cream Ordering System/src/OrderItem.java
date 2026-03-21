public class OrderItem {
    /** Enum for cone type */
    private enum ConeTypes {
        WAFFLE_CONE,
        CUP
    }

    private ConeTypes coneType;
    private int quantity;
    private String edits;

    // Default constructor requiring cone type, quantity, and edits
    public OrderItem(ConeTypes coneType, int quantity, String edits) {
        this.coneType = coneType;
        this.quantity = quantity;
        this.edits = edits;
    }

    // Getter and Setter Methods
    public ConeTypes getName() {
        return coneType;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getEdits() {
        return edits;
    }

    public void setName(ConeTypes newConeType) {
        coneType = newConeType;
    }

    public void setQuantity(int newQuantity) {
        quantity = newQuantity;
    }

    public void setEdits(String newEdits) {
        edits = newEdits;
    }
}
