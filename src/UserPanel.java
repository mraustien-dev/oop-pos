import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class UserPanel extends JPanel {
    private Store store;
    private User loggedIn;
    private DefaultTableModel model = new DefaultTableModel(new String[] {"Username", "Full Name", "Role"}, 0);
    private JTable table = new JTable(model);

    public UserPanel(Store store, User loggedIn) {
        this.store = store;
        this.loggedIn = loggedIn;
        Style.styleTable(table);

        JButton addButton = Style.button("+ Add User", Style.BLUE);
        JButton editButton = Style.button("Edit", Style.GRAY);
        JButton deleteButton = Style.button("Delete", Style.RED);
        addButton.addActionListener(e -> addUser());
        editButton.addActionListener(e -> editUser());
        deleteButton.addActionListener(e -> deleteUser());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttons.setOpaque(false);
        buttons.add(addButton);
        buttons.add(editButton);
        buttons.add(deleteButton);

        JPanel card = Style.card("Manage Users");
        card.add(Style.scroll(table), BorderLayout.CENTER);
        card.add(buttons, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(card, BorderLayout.CENTER);

        refresh();
    }

    private void addUser() {
        JTextField usernameField = Style.textField();
        JTextField fullNameField = Style.textField();
        JPasswordField passwordField = new JPasswordField();
        JPasswordField confirmField = new JPasswordField();
        Style.styleField(passwordField);
        Style.styleField(confirmField);
        JComboBox<String> roleBox = new JComboBox<>(new String[] {"Cashier", "Admin"});

        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
        form.add(new JLabel("Username:"));
        form.add(usernameField);
        form.add(new JLabel("Full Name:"));
        form.add(fullNameField);
        form.add(new JLabel("Password:"));
        form.add(passwordField);
        form.add(new JLabel("Confirm Password:"));
        form.add(confirmField);
        form.add(new JLabel("Role:"));
        form.add(roleBox);

        int choice = JOptionPane.showConfirmDialog(this, form, "Add User", JOptionPane.OK_CANCEL_OPTION);
        if (choice != JOptionPane.OK_OPTION) {
            return;
        }
        String password = new String(passwordField.getPassword());
        String confirm = new String(confirmField.getPassword());
        if (!password.equals(confirm)) {
            showError("The passwords do not match.");
            return;
        }
        try {
            store.addUser(usernameField.getText(), password, fullNameField.getText(),
                    (String) roleBox.getSelectedItem());
            refresh();
            JOptionPane.showMessageDialog(this, "User added.");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private User getSelectedUser() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pick a user first.");
            return null;
        }
        return store.getUsers().get(row);
    }

    private void editUser() {
        User user = getSelectedUser();
        if (user == null) {
            return;
        }
        JTextField fullNameField = Style.textField();
        fullNameField.setText(user.getFullName());
        JPasswordField passwordField = new JPasswordField();
        JPasswordField confirmField = new JPasswordField();
        Style.styleField(passwordField);
        Style.styleField(confirmField);
        JComboBox<String> roleBox = new JComboBox<>(new String[] {"Cashier", "Admin"});
        roleBox.setSelectedItem(user.getRole());

        JPanel form = new JPanel(new GridLayout(6, 2, 8, 8));
        form.add(new JLabel("Username:"));
        form.add(new JLabel(user.getUsername()));
        form.add(new JLabel("Full Name:"));
        form.add(fullNameField);
        form.add(new JLabel("New Password:"));
        form.add(passwordField);
        form.add(new JLabel("Confirm Password:"));
        form.add(confirmField);
        form.add(new JLabel("Role:"));
        form.add(roleBox);
        form.add(Style.smallLabel("Leave password blank to keep it."));

        int choice = JOptionPane.showConfirmDialog(this, form, "Edit User", JOptionPane.OK_CANCEL_OPTION);
        if (choice != JOptionPane.OK_OPTION) {
            return;
        }
        String password = new String(passwordField.getPassword());
        String confirm = new String(confirmField.getPassword());
        if (!password.equals(confirm)) {
            showError("The passwords do not match.");
            return;
        }
        try {
            store.updateUser(user, fullNameField.getText(), password,
                    (String) roleBox.getSelectedItem(), loggedIn);
            refresh();
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private void deleteUser() {
        User user = getSelectedUser();
        if (user == null) {
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this,
                "Delete user " + user.getUsername() + " (" + user.getFullName() + ")?",
                "Delete", JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            store.deleteUser(user, loggedIn);
            refresh();
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Oops", JOptionPane.WARNING_MESSAGE);
    }

    public void refresh() {
        model.setRowCount(0);
        for (User u : store.getUsers()) {
            model.addRow(new Object[] {u.getUsername(), u.getFullName(), u.getRole()});
        }
    }
}
