// ABSTRACTION + INHERITANCE
// User is "abstract": you cannot create a plain User.
// You can only create an Admin or a Cashier (they extend User).
public abstract class User {
    // ENCAPSULATION: fields are private, other classes use the getters.
    private String username;
    private String password;
    private String fullName;

    public User(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    // Setters check the value first (ENCAPSULATION).
    public void setFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty.");
        }
        this.fullName = fullName.trim();
    }

    public void setPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        this.password = password;
    }

    // Abstract methods: no body here. Admin and Cashier must write their own.
    public abstract String getRole();

    public abstract boolean canManageProducts();
}
