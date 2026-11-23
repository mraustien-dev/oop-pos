public class Cashier extends User {

    public Cashier(String username, String password, String fullName) {
        super(username, password, fullName);
    }

    @Override
    public String getRole() {
        return "Cashier";
    }

    @Override
    public boolean canManageProducts() {
        return false;
    }
}
