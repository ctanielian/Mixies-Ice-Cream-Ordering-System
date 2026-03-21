public class IceCream extends MenuItem {
    private String flavor;

    // Default Constructor for Ice Cream
    public IceCream(String name, double price, int stockQuantity, String[] allergens, String flavor) {
        super(name, price, stockQuantity, allergens);
        this.flavor = flavor;
    }

    // Getter and Setter Methods
    /**
     * Method to set the topping type
     * 
     * @param newTopping The new topping type to be set
     */
    public void setTopping(String newFlavor) {
        flavor = newFlavor;
    }

    /**
     * Method to get the current topping type
     * 
     * @return the current topping type
     */
    public String getTopping() {
        return flavor;
    }
}
