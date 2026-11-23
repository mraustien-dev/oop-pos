// INHERITANCE: Admin gets username, password and fullName from User.
public class Admin extends User {

    public Admin(String username, String password, String fullName) {
        super(username, password, fullName); // calls the User constructor
    }

    // POLYMORPHISM (overriding): Admin gives its own answer.
    @Override
    public String getRole() {
        return "Admin";
    }

    @Override
    public boolean canManageProducts() {
        return true;
    }
}
