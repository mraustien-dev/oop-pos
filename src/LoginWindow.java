import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

// The first window: asks for username and password.
// INHERITANCE: LoginWindow "is a" JFrame (a window).
public class LoginWindow extends JFrame {
    private Store store;
    private JTextField usernameField = Style.textField();
    private JPasswordField passwordField = new JPasswordField();

    public LoginWindow(Store store) {
        this.store = store;

        setTitle("JavaPOS - Login");
        setSize(640, 360);
        setResizable(false);
        setLocationRelativeTo(null); // center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Style.styleField(passwordField);

        // ----- Left side: blue panel with the app name -----
        JLabel appName = new JLabel("JavaPOS");
        appName.setFont(Style.boldFont(36));
        appName.setForeground(Style.WHITE);
        JLabel subtitle = new JLabel("Point of Sale System");
        subtitle.setFont(Style.font(16));
        subtitle.setForeground(Style.WHITE);

        JPanel left = new JPanel(new GridLayout(2, 1));
        left.setBackground(Style.BLUE);
        left.setBorder(BorderFactory.createEmptyBorder(100, 30, 100, 30));
        left.add(appName);
        left.add(subtitle);

        // ----- Right side: the login form -----
        JButton loginButton = Style.button("Log In", Style.BLUE);
        loginButton.addActionListener(e -> login()); // run login() when clicked
        getRootPane().setDefaultButton(loginButton); // pressing Enter clicks Log In

        JLabel welcome = Style.title("Welcome back!");
        welcome.setFont(Style.boldFont(22));

        JPanel form = new JPanel(new GridLayout(7, 1, 0, 6)); // 7 rows, 1 column
        form.setBackground(Style.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        form.add(welcome);
        form.add(Style.smallLabel("Please log in to continue"));
        form.add(new JLabel("Username"));
        form.add(usernameField);
        form.add(new JLabel("Password"));
        form.add(passwordField);
        form.add(loginButton);

        // Left and right side by side
        JPanel main = new JPanel(new GridLayout(1, 2));
        main.add(left);
        main.add(form);
        add(main);
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        User user = store.login(username, password);
        if (user == null) {
            JOptionPane.showMessageDialog(this, "Wrong username or password.", "Login failed",
                    JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
            return;
        }

        new MainWindow(store, user).setVisible(true);
        dispose(); // close the login window
    }
}
