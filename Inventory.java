public class Inventory {

    private Product[] products;
    private int count;

    // Constructor
    public Inventory(int size) {
        products = new Product[size];
        count = 0;
    }

    // Add product
    public boolean addProduct(Product product) {

        if (count == products.length) {
            return false;
        }

        if (findProduct(product.getId()) != null) {
            return false;
        }

        products[count] = product;
        count++;

        return true;
    }

    // Search for product
    public Product findProduct(String id) {

        for (int i = 0; i < count; i++) {

            if (products[i].getId().equalsIgnoreCase(id)) {
                return products[i];
            }
        }

        return null;
    }

    // Delete product
    public boolean deleteProduct(String id) {

        for (int i = 0; i < count; i++) {

            if (products[i].getId().equalsIgnoreCase(id)) {

                for (int j = i; j < count - 1; j++) {
                    products[j] = products[j + 1];
                }

                count--;
                products[count] = null;

                return true;
            }
        }

        return false;
    }

    // Update product quantity
    public boolean updateQuantity(String id, int quantity) {

        Product product = findProduct(id);

        if (product != null && quantity >= 0) {
            product.setQuantity(quantity);
            return true;
        }

        return false;
    }

    // Return all products
    public Product[] getProducts() {

        Product[] result = new Product[count];

        for (int i = 0; i < count; i++) {
            result[i] = products[i];
        }

        return result;
    }

    // Count products
    public int getCount() {
        return count;
    }

    // Calculate total inventory value
    public double getTotalValue() {

        double total = 0;

        for (int i = 0; i < count; i++) {
            total += products[i].getTotalValue();
        }

        return total;
    }

    // Count low-stock products
    public int getLowStockCount() {

        int low = 0;

        for (int i = 0; i < count; i++) {

            if (products[i].getQuantity() <= 5) {
                low++;
            }
        }

        return low;
    }
}