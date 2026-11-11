public class Product {

    private String id;
    private String name;
    private String category;
    private double price;
    private int quantity;

    // Constructor
    public Product(String id, String name, String category,
                    double price, int quantity) {

        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    // Update quantity
    public void setQuantity(int quantity) {
        if (quantity >= 0) {
            this.quantity = quantity;
        }
    }

    // Calculate total value of product stock
    public double getTotalValue() {
        return price * quantity;
    }

    // Check stock availability
    public String getStatus() {

        if (quantity == 0) {
            return "Out of Stock";
        } else if (quantity <= 5) {
            return "Low Stock";
        } else {
            return "Available";
        }
    }
}