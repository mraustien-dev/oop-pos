import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class MainWindow extends JFrame {
    private Store store;
    private SellPanel sellPanel;
    private ProductPanel productPanel;
    private UserPanel userPanel;
    private HistoryPanel historyPanel;

    public MainWindow(Store store, User user) {
        this.store = store;

        setTitle("JavaPOS - " + user.getRole());
        setSize(1100, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel appName = new JLabel("JavaPOS");
        appName.setFont(Style.boldFont(22));
        appName.setForeground(Style.WHITE);

        JLabel userLabel = new JLabel(user.getFullName() + "  |  " + user.getRole());
        userLabel.setForeground(Style.WHITE);

        JButton logoutButton = Style.button("Log Out", Style.RED);
        logoutButton.addActionListener(e -> logout());

        JPanel rightSide = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        rightSide.setOpaque(false);
        rightSide.add(userLabel);
        rightSide.add(logoutButton);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Style.DARK);
        topBar.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        topBar.add(appName, BorderLayout.WEST);
        topBar.add(rightSide, BorderLayout.EAST);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        sellPanel = new SellPanel(store, user);
        tabs.addTab("   Sell   ", sellPanel);

        if (user.canManageProducts()) {
            productPanel = new ProductPanel(store);
            tabs.addTab("   Products   ", productPanel);
            userPanel = new UserPanel(store, user);
            tabs.addTab("   Users   ", userPanel);
        }

        historyPanel = new HistoryPanel(store);
        tabs.addTab("   History   ", historyPanel);

        tabs.addChangeListener(e -> refreshAll());

        add(topBar, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private void refreshAll() {
        sellPanel.refresh();
        historyPanel.refresh();
        if (productPanel != null) {
            productPanel.refresh();
        }
        if (userPanel != null) {
            userPanel.refresh();
        }
    }

    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this, "Do you want to log out?", "Log Out",
                JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            new LoginWindow(store).setVisible(true);
            dispose();
        }
    }
}
