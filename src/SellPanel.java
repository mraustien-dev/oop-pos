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

public class SellPanel extends JPanel {
    private Store store;
    private User user;
    private Cart cart = new Cart();

    private DefaultTableModel productModel = new DefaultTableModel(new String[] {"ID", "Name", "Price", "Stock"}, 0);
    private JTable productTable = new JTable(productModel);

    private DefaultTableModel cartModel = new DefaultTableModel(new String[] {"Name", "Price", "Qty", "Subtotal"}, 0);
    private JTable cartTable = new JTable(cartModel);

    private JLabel totalLabel = new JLabel("PHP 0.00");
    private JTextField cashField = Style.textField();

    public SellPanel(Store store, User user) {
        this.store = store;
        this.user = user;

        Style.styleTable(productTable);
        Style.styleTable(cartTable);

        JButton addOneButton = Style.button("+ Add 1", Style.BLUE);
        JButton addManyButton = Style.button("+ Add Quantity", Style.GRAY);
        addOneButton.addActionListener(e -> addOne());
        addManyButton.addActionListener(e -> addMany());

        JPanel productButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        productButtons.setOpaque(false);
        productButtons.add(addOneButton);
        productButtons.add(addManyButton);

        JPanel left = Style.card("Products");
        left.add(Style.scroll(productTable), BorderLayout.CENTER);
        left.add(productButtons, BorderLayout.SOUTH);

        JButton removeButton = Style.button("Remove Item", Style.RED);
        JButton clearButton = Style.button("Clear Cart", Style.GRAY);
        JButton payButton = Style.button("PAY", Style.GREEN);
        payButton.setFont(Style.boldFont(16));
        removeButton.addActionListener(e -> removeItem());
        clearButton.addActionListener(e -> clearCart());
        payButton.addActionListener(e -> pay());
        cashField.addActionListener(e -> pay());

        JPanel cartButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        cartButtons.setOpaque(false);
        cartButtons.add(removeButton);
        cartButtons.add(clearButton);

        totalLabel.setFont(Style.boldFont(28));
        totalLabel.setForeground(Style.GREEN);
        JPanel totalRow = new JPanel(new BorderLayout());
        totalRow.setOpaque(false);
        totalRow.add(Style.smallLabel("TOTAL"), BorderLayout.WEST);
        totalRow.add(totalLabel, BorderLayout.EAST);

        JPanel payRow = new JPanel(new BorderLayout(8, 0));
        payRow.setOpaque(false);
        payRow.add(new JLabel("Cash:"), BorderLayout.WEST);
        payRow.add(cashField, BorderLayout.CENTER);
        payRow.add(payButton, BorderLayout.EAST);

        JPanel bottom = new JPanel(new GridLayout(3, 1, 0, 10));
        bottom.setOpaque(false);
        bottom.add(cartButtons);
        bottom.add(totalRow);
        bottom.add(payRow);

        JPanel right = Style.card("Cart");
        right.add(Style.scroll(cartTable), BorderLayout.CENTER);
        right.add(bottom, BorderLayout.SOUTH);

        setLayout(new GridLayout(1, 2, 14, 0));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(left);
        add(right);

        refresh();
    }

    private Product getSelectedProduct() {
        int row = productTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pick a product first.");
            return null;
        }
        return store.getProducts().get(row);
    }

    private void addOne() {
        Product product = getSelectedProduct();
        if (product == null) {
            return;
        }
        try {
            cart.addItem(product);
            refreshCart();
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private void addMany() {
        Product product = getSelectedProduct();
        if (product == null) {
            return;
        }
        String input = JOptionPane.showInputDialog(this, "How many " + product.getName() + "?");
        if (input == null) {
            return;
        }
        try {
            int quantity = Integer.parseInt(input.trim());
            cart.addItem(product, quantity);
            refreshCart();
        } catch (NumberFormatException e) {
            showError("Please enter a whole number.");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private void removeItem() {
        int row = cartTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pick an item in the cart first.");
            return;
        }
        cart.removeItem(row);
        refreshCart();
    }

    private void clearCart() {
        cart.clear();
        refreshCart();
    }

    private void pay() {
        try {
            double cash = Double.parseDouble(cashField.getText().trim());
            Transaction transaction = store.checkout(cart, cash, user);
            JOptionPane.showMessageDialog(this, Style.receiptBox(transaction.getReceipt()),
                    "Payment Successful", JOptionPane.PLAIN_MESSAGE);
            cashField.setText("");
            refresh();
        } catch (NumberFormatException e) {
            showError("Please enter a valid cash amount.");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Oops", JOptionPane.WARNING_MESSAGE);
    }

    public void refresh() {
        productModel.setRowCount(0);
        for (Product p : store.getProducts()) {
            productModel.addRow(new Object[] {p.getId(), p.getName(), String.format("%.2f", p.getPrice()), p.getStock()});
        }
        refreshCart();
    }

    private void refreshCart() {
        cartModel.setRowCount(0);
        for (CartItem item : cart.getItems()) {
            cartModel.addRow(new Object[] {
                item.getProduct().getName(),
                String.format("%.2f", item.getProduct().getPrice()),
                item.getQuantity(),
                String.format("%.2f", item.getSubtotal())
            });
        }
        totalLabel.setText("PHP " + String.format("%.2f", cart.getTotal()));
    }
}
