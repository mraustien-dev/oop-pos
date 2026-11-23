// INHERITANCE: Cashier gets username, password and fullName from User.
public class Cashier extends User {

    public Cashier(String username, String password, String fullName) {
        super(username, password, fullName); // calls the User constructor
    }

    // POLYMORPHISM (overriding): same method names as Admin, different answers.
    @Override
    public String getRole() {
        return "Cashier";
    }

    @Override
    public boolean canManageProducts() {
        return false;
    }
}
