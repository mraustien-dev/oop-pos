import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

// The "Products" tab (Admin only): add, edit, restock and delete products.
public class ProductPanel extends JPanel {
    private Store store;
    private DefaultTableModel model = new DefaultTableModel(new String[] {"ID", "Name", "Price", "Stock"}, 0);
    private JTable table = new JTable(model);

    public ProductPanel(Store store) {
        this.store = store;
        Style.styleTable(table);

        JButton addButton = Style.button("+ Add Product", Style.BLUE);
        JButton editButton = Style.button("Edit", Style.GRAY);
        JButton restockButton = Style.button("Restock", Style.GREEN);
        JButton deleteButton = Style.button("Delete", Style.RED);
        addButton.addActionListener(e -> addProduct());
        editButton.addActionListener(e -> editProduct());
        restockButton.addActionListener(e -> restockProduct());
        deleteButton.addActionListener(e -> deleteProduct());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttons.setOpaque(false);
        buttons.add(addButton);
        buttons.add(editButton);
        buttons.add(restockButton);
        buttons.add(deleteButton);

        JPanel card = Style.card("Manage Products");
        card.add(Style.scroll(table), BorderLayout.CENTER);
        card.add(buttons, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(card, BorderLayout.CENTER);

        refresh();
    }

    private Product getSelectedProduct() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pick a product first.");
            return null;
        }
        return store.getProducts().get(row);
    }

    private void addProduct() {
        JTextField nameField = Style.textField();
        JTextField priceField = Style.textField();
        JTextField stockField = Style.textField();

        JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
        form.add(new JLabel("Name:"));
        form.add(nameField);
        form.add(new JLabel("Price:"));
        form.add(priceField);
        form.add(new JLabel("Stock:"));
        form.add(stockField);

        int choice = JOptionPane.showConfirmDialog(this, form, "Add Product", JOptionPane.OK_CANCEL_OPTION);
        if (choice != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            double price = Double.parseDouble(priceField.getText().trim());
            int stock = Integer.parseInt(stockField.getText().trim());
            store.addProduct(nameField.getText(), price, stock);
            refresh();
        } catch (NumberFormatException e) {
            showError("Price and stock must be numbers.");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private void editProduct() {
        Product product = getSelectedProduct();
        if (product == null) {
            return;
        }
        JTextField nameField = Style.textField();
        JTextField priceField = Style.textField();
        nameField.setText(product.getName());
        priceField.setText(String.format("%.2f", product.getPrice()));

        JPanel form = new JPanel(new GridLayout(2, 2, 8, 8));
        form.add(new JLabel("Name:"));
        form.add(nameField);
        form.add(new JLabel("Price:"));
        form.add(priceField);

        int choice = JOptionPane.showConfirmDialog(this, form, "Edit Product", JOptionPane.OK_CANCEL_OPTION);
        if (choice != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            double price = Double.parseDouble(priceField.getText().trim());
            store.updateProduct(product, nameField.getText(), price);
            refresh();
        } catch (NumberFormatException e) {
            showError("Price must be a number.");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private void restockProduct() {
        Product product = getSelectedProduct();
        if (product == null) {
            return;
        }
        String input = JOptionPane.showInputDialog(this, "How many " + product.getName() + " to add?");
        if (input == null) {
            return;
        }
        try {
            store.restock(product, Integer.parseInt(input.trim()));
            refresh();
        } catch (NumberFormatException e) {
            showError("Please enter a whole number.");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private void deleteProduct() {
        Product product = getSelectedProduct();
        if (product == null) {
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this, "Delete " + product.getName() + "?",
                "Delete", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            store.deleteProduct(product);
            refresh();
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Oops", JOptionPane.WARNING_MESSAGE);
    }

    public void refresh() {
        model.setRowCount(0);
        for (Product p : store.getProducts()) {
            model.addRow(new Object[] {p.getId(), p.getName(), String.format("%.2f", p.getPrice()), p.getStock()});
        }
    }
}
