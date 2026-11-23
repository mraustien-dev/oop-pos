import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

// The "History" tab: two summary boxes on top, list of all past sales below.
public class HistoryPanel extends JPanel {
    private Store store;
    private DefaultTableModel model = new DefaultTableModel(new String[] {"ID", "Date", "Cashier", "Total"}, 0);
    private JTable table = new JTable(model);
    private JLabel totalSalesLabel = new JLabel();
    private JLabel countLabel = new JLabel();

    public HistoryPanel(Store store) {
        this.store = store;
        Style.styleTable(table);

        // ----- Top: two summary boxes -----
        JPanel stats = new JPanel(new GridLayout(1, 2, 14, 0));
        stats.add(Style.statCard("Total Sales", totalSalesLabel, Style.GREEN));
        stats.add(Style.statCard("Transactions", countLabel, Style.BLUE));

        // ----- Bottom: the table -----
        JButton viewButton = Style.button("View Receipt", Style.BLUE);
        viewButton.addActionListener(e -> viewReceipt());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttons.setOpaque(false);
        buttons.add(viewButton);

        JPanel card = Style.card("Sales History");
        card.add(Style.scroll(table), BorderLayout.CENTER);
        card.add(buttons, BorderLayout.SOUTH);

        setLayout(new BorderLayout(0, 14));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(stats, BorderLayout.NORTH);
        add(card, BorderLayout.CENTER);

        refresh();
    }

    private void viewReceipt() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pick a transaction first.");
            return;
        }
        Transaction transaction = store.getTransactions().get(row);
        JOptionPane.showMessageDialog(this, Style.receiptBox(transaction.getReceipt()),
                "Receipt " + transaction.getId(), JOptionPane.PLAIN_MESSAGE);
    }

    public void refresh() {
        model.setRowCount(0);
        for (Transaction t : store.getTransactions()) {
            model.addRow(new Object[] {t.getId(), t.getDateTime(), t.getCashier(), String.format("%.2f", t.getTotal())});
        }
        totalSalesLabel.setText("PHP " + String.format("%.2f", store.getTotalSales()));
        countLabel.setText(String.valueOf(store.getTransactions().size()));
    }
}
