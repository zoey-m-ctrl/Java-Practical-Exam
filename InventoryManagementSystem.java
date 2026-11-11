import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class InventoryManagementSystem extends JFrame {

    Inventory inventory = new Inventory(100);

    JTextField idField, nameField, categoryField;
    JTextField priceField, quantityField, searchField;

    JTable table;
    DefaultTableModel model;
    JLabel summary;

    // Constructor
    public InventoryManagementSystem() {

        setTitle("Inventory Management System");
        setSize(950, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Table columns
        String[] columns = {
                "ID", "Name", "Category", "Price (UGX)",
                "Quantity", "Status", "Total Value (UGX)"
        };

        model = new DefaultTableModel(columns, 0) {

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(25);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15));

        mainPanel.add(createInputPanel(), BorderLayout.NORTH);

        mainPanel.add(
                new JScrollPane(table),
                BorderLayout.CENTER);

        mainPanel.add(createBottomPanel(), BorderLayout.SOUTH);

        add(mainPanel);

        // Sample products
        inventory.addProduct(
                new Product("P001", "Exercise Book", "Stationery", 2500, 30));

        inventory.addProduct(
                new Product("P002", "Blue Pen", "Stationery", 1000, 4));

        inventory.addProduct(
                new Product("P003", "USB Flash Disk", "Electronics", 18000, 0));

        displayProducts(inventory.getProducts());
    }

    // Create product input panel
    public JPanel createInputPanel() {

        JPanel panel = new JPanel(new BorderLayout(5, 5));

        JLabel heading = new JLabel("PRODUCT INFORMATION");

        heading.setFont(new Font("Arial", Font.BOLD, 18));

        panel.add(heading, BorderLayout.NORTH);

        JPanel fields = new JPanel(new GridLayout(2, 5, 5, 5));

        idField = new JTextField();
        nameField = new JTextField();
        categoryField = new JTextField();
        priceField = new JTextField();
        quantityField = new JTextField();

        fields.add(new JLabel("Product ID"));
        fields.add(new JLabel("Product Name"));
        fields.add(new JLabel("Category"));
        fields.add(new JLabel("Price"));
        fields.add(new JLabel("Quantity"));

        fields.add(idField);
        fields.add(nameField);
        fields.add(categoryField);
        fields.add(priceField);
        fields.add(quantityField);

        panel.add(fields, BorderLayout.CENTER);

        JPanel buttons = new JPanel();

        JButton addButton = new JButton("Add Product");
        JButton updateButton = new JButton("Update Stock");
        JButton deleteButton = new JButton("Delete Product");
        JButton clearButton = new JButton("Clear");

        buttons.add(addButton);
        buttons.add(updateButton);
        buttons.add(deleteButton);
        buttons.add(clearButton);

        addButton.addActionListener(e -> addProduct());
        updateButton.addActionListener(e -> updateStock());
        deleteButton.addActionListener(e -> deleteProduct());
        clearButton.addActionListener(e -> clearFields());

        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    // Create search and summary panel
    public JPanel createBottomPanel() {

        JPanel panel = new JPanel(new BorderLayout());

        JPanel searchPanel = new JPanel();

        searchField = new JTextField(15);

        JButton searchButton = new JButton("Search");
        JButton showAllButton = new JButton("Show All");

        searchPanel.add(new JLabel("Search Product:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);

        searchButton.addActionListener(e -> searchProduct());

        showAllButton.addActionListener(
                e -> displayProducts(inventory.getProducts()));

        summary = new JLabel();

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(summary, BorderLayout.SOUTH);

        return panel;
    }

    // Add a new product
    public void addProduct() {

        try {

            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String category = categoryField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || category.isEmpty()
                    || priceField.getText().trim().isEmpty()
                    || quantityField.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(
                        this, "Please fill in all fields.");

                return;
            }

            double price = Double.parseDouble(priceField.getText());
            int quantity = Integer.parseInt(quantityField.getText());

            if (price < 0 || quantity < 0) {

                JOptionPane.showMessageDialog(
                        this, "Price and quantity cannot be negative.");

                return;
            }

            Product product = new Product(
                    id, name, category, price, quantity);

            if (inventory.addProduct(product)) {

                displayProducts(inventory.getProducts());
                clearFields();

                JOptionPane.showMessageDialog(
                        this, "Product added successfully!");

            } else {

                JOptionPane.showMessageDialog(
                        this, "Product ID already exists or inventory is full.");
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this, "Please enter valid price and quantity.");
        }
    }

    // Update stock quantity
    public void updateStock() {

        String id = idField.getText().trim();

        // Get ID from selected table row if field is empty
        if (id.isEmpty()) {

            int row = table.getSelectedRow();

            if (row >= 0) {
                id = model.getValueAt(row, 0).toString();
            }
        }

        if (id.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this, "Enter a product ID or select a product.");

            return;
        }

        try {

            int quantity = Integer.parseInt(quantityField.getText());

            if (quantity < 0) {

                JOptionPane.showMessageDialog(
                        this, "Quantity cannot be negative.");

                return;
            }

            if (inventory.updateQuantity(id, quantity)) {

                displayProducts(inventory.getProducts());

                JOptionPane.showMessageDialog(
                        this, "Stock updated successfully!");

            } else {

                JOptionPane.showMessageDialog(
                        this, "Product not found.");
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this, "Enter a valid quantity.");
        }
    }

    // Delete selected product
    public void deleteProduct() {

        int row = table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this, "Please select a product to delete.");

            return;
        }

        String id = model.getValueAt(row, 0).toString();

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this product?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {

            inventory.deleteProduct(id);

            displayProducts(inventory.getProducts());

            clearFields();
        }
    }

    // Search for a product
    public void searchProduct() {

        String search = searchField.getText().trim();

        Product[] allProducts = inventory.getProducts();

        Product[] results = new Product[allProducts.length];

        int count = 0;

        for (int i = 0; i < allProducts.length; i++) {

            if (allProducts[i].getId().toLowerCase()
                    .contains(search.toLowerCase())
                    || allProducts[i].getName().toLowerCase()
                            .contains(search.toLowerCase())) {

                results[count] = allProducts[i];
                count++;
            }
        }

        Product[] found = new Product[count];

        for (int i = 0; i < count; i++) {
            found[i] = results[i];
        }

        displayProducts(found);
    }

    // Display products in the table
    public void displayProducts(Product[] products) {

        model.setRowCount(0);

        for (int i = 0; i < products.length; i++) {

            Product p = products[i];

            model.addRow(new Object[] {
                    p.getId(),
                    p.getName(),
                    p.getCategory(),
                    String.format("%,.2f", p.getPrice()),
                    p.getQuantity(),
                    p.getStatus(),
                    String.format("%,.2f", p.getTotalValue())
            });
        }

        summary.setText(
                "Total Products: " + inventory.getCount()
                        + "     |     Low Stock: " + inventory.getLowStockCount()
                        + "     |     Total Inventory Value: UGX "
                        + String.format("%,.2f", inventory.getTotalValue()));
    }

    // Clear input fields
    public void clearFields() {

        idField.setText("");
        nameField.setText("");
        categoryField.setText("");
        priceField.setText("");
        quantityField.setText("");

        table.clearSelection();
    }

    // Main method
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            InventoryManagementSystem system = new InventoryManagementSystem();

            system.setVisible(true);
        });
    }
}