public class Topping extends MenuItem {
    /** Enum for type of topping */
    private enum ToppingTypes {
        SAUCE,
        CRUMBLE,
        FRUIT,
        CANDY
    }

    private ToppingTypes toppingType;

    // Default Constructor for type
    public Topping(String name, double price, int stockQuantity, String[] allergens, ToppingTypes type) {
        super(name, price, stockQuantity, allergens);
        toppingType = type;
    }

    // Getter and Setter Methods
    /**
     * Method to set the topping type
     * 
     * @param newTopping The new topping type to be set
     */
    public void setTopping(ToppingTypes newType) {
        toppingType = newType;
    }

    /**
     * Method to get the current topping type
     * 
     * @return the current topping type
     */
    public ToppingTypes getTopping() {
        return toppingType;
    }
}
