public class MenuItem {
    private String name;
    private double price;
    private int stockQuantity;
    private String[] allergens;

    // Default constructor requiring name, price, stockQuantity, and allergens
    public MenuItem(String name, double price, int stockQuantity, String[] allergens) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.allergens = allergens;
    }

    // Getter and Setter Methods
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public String[] getAllergens() {
        return allergens;
    }

    public void setName(String newName) {
        name = newName;
    }

    public void setPrice(double newPrice) {
        price = newPrice;
    }

    public void setStockQuantity(int newQuantity) {
        stockQuantity = newQuantity;
    }

    public void setAllergens(String[] newAllergens) {
        allergens = newAllergens;
    }
}
