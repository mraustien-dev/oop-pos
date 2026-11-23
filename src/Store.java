import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Store {
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Product> products = new ArrayList<>();
    private ArrayList<Transaction> transactions = new ArrayList<>();
    private FileManager files = new FileManager("data");

    public Store() {
        loadUsers();
        loadProducts();
        loadTransactions();
    }

    public User login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.checkPassword(password)) {
                return user;
            }
        }
        return null;
    }

    public void addUser(String username, String password, String fullName, String role) {
        username = username.trim();
        fullName = fullName.trim();
        if (username.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
            throw new IllegalArgumentException("Please fill in all the fields.");
        }
        if (username.contains(",") || password.contains(",") || fullName.contains(",")) {
            throw new IllegalArgumentException("Commas are not allowed.");
        }
        if (username.contains(" ")) {
            throw new IllegalArgumentException("Username cannot have spaces.");
        }
        if (findUser(username) != null) {
            throw new IllegalArgumentException("Username \"" + username + "\" is already taken.");
        }
        if (role.equals("Admin")) {
            users.add(new Admin(username, password, fullName));
        } else {
            users.add(new Cashier(username, password, fullName));
        }
        saveUsers();
    }

    public void updateUser(User user, String fullName, String newPassword, String role, User loggedIn) {
        fullName = fullName.trim();
        if (fullName.isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty.");
        }
        if (fullName.contains(",") || newPassword.contains(",")) {
            throw new IllegalArgumentException("Commas are not allowed.");
        }
        boolean roleChanged = !user.getRole().equals(role);
        if (roleChanged && user == loggedIn) {
            throw new IllegalArgumentException("You cannot change your own role.");
        }
        if (roleChanged && user.getRole().equals("Admin") && countAdmins() == 1) {
            throw new IllegalArgumentException("There must be at least one Admin.");
        }

        String password = newPassword.isEmpty() ? user.getPassword() : newPassword;
        if (roleChanged) {
            User replacement;
            if (role.equals("Admin")) {
                replacement = new Admin(user.getUsername(), password, fullName);
            } else {
                replacement = new Cashier(user.getUsername(), password, fullName);
            }
            users.set(users.indexOf(user), replacement);
        } else {
            user.setFullName(fullName);
            user.setPassword(password);
        }
        saveUsers();
    }

    public void deleteUser(User user, User loggedIn) {
        if (user == loggedIn) {
            throw new IllegalArgumentException("You cannot delete your own account while logged in.");
        }
        if (user.getRole().equals("Admin") && countAdmins() == 1) {
            throw new IllegalArgumentException("There must be at least one Admin.");
        }
        users.remove(user);
        saveUsers();
    }

    private int countAdmins() {
        int count = 0;
        for (User user : users) {
            if (user.getRole().equals("Admin")) {
                count++;
            }
        }
        return count;
    }

    public User findUser(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void addProduct(String name, double price, int stock) {
        Product product = new Product(nextProductId(), name, price, stock);
        products.add(product);
        saveProducts();
    }

    public void updateProduct(Product product, String name, double price) {
        product.update(name, price);
        saveProducts();
    }

    public void restock(Product product, int quantity) {
        product.addStock(quantity);
        saveProducts();
    }

    public void deleteProduct(Product product) {
        products.remove(product);
        saveProducts();
    }

    private String nextProductId() {
        int number = 1;
        while (findProduct("P" + number) != null) {
            number++;
        }
        return "P" + number;
    }

    public Product findProduct(String id) {
        for (Product product : products) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        return null;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public Transaction checkout(Cart cart, double cash, User cashier) {
        if (cart.isEmpty()) {
            throw new IllegalArgumentException("The cart is empty.");
        }
        double total = cart.getTotal();
        if (cash < total) {
            throw new IllegalArgumentException("Not enough cash. Total is PHP " + String.format("%.2f", total));
        }
        for (CartItem item : cart.getItems()) {
            if (!products.contains(item.getProduct())) {
                throw new IllegalArgumentException(item.getProduct().getName() + " was deleted. Remove it from the cart.");
            }
        }

        String itemsText = "";
        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            product.removeStock(item.getQuantity());
            if (!itemsText.isEmpty()) {
                itemsText += ";";
            }
            itemsText += product.getName() + " x" + item.getQuantity()
                    + " = " + String.format("%.2f", item.getSubtotal());
        }

        String id = "T" + (transactions.size() + 1);
        Transaction transaction = new Transaction(id, now(), cashier.getUsername(), total, cash, itemsText);
        transactions.add(transaction);

        saveProducts();
        saveTransactions();
        cart.clear();
        return transaction;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public double getTotalSales() {
        double total = 0;
        for (Transaction t : transactions) {
            total = total + t.getTotal();
        }
        return total;
    }

    private String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    private void loadUsers() {
        for (String line : files.readLines("users.csv")) {
            String[] parts = line.split(",");
            if (parts.length < 4) {
                continue;
            }
            if (parts[3].equals("Admin")) {
                users.add(new Admin(parts[0], parts[1], parts[2]));
            } else {
                users.add(new Cashier(parts[0], parts[1], parts[2]));
            }
        }
        if (users.isEmpty()) {
            users.add(new Admin("admin", "admin123", "Store Admin"));
            users.add(new Cashier("cashier", "cashier123", "Store Cashier"));
            saveUsers();
        }
    }

    private void saveUsers() {
        ArrayList<String> lines = new ArrayList<>();
        for (User u : users) {
            lines.add(u.getUsername() + "," + u.getPassword() + "," + u.getFullName() + "," + u.getRole());
        }
        files.writeLines("users.csv", lines);
    }

    private void loadProducts() {
        if (!files.exists("products.csv")) {
            products.add(new Product("P1", "Coke 330ml", 20.00, 50));
            products.add(new Product("P2", "Bread Loaf", 45.00, 20));
            products.add(new Product("P3", "Instant Noodles", 15.00, 100));
            products.add(new Product("P4", "Bottled Water", 12.00, 80));
            saveProducts();
            return;
        }
        for (String line : files.readLines("products.csv")) {
            String[] parts = line.split(",");
            try {
                products.add(new Product(parts[0], parts[1],
                        Double.parseDouble(parts[2]), Integer.parseInt(parts[3])));
            } catch (Exception e) {
                System.out.println("Skipped a bad product line: " + line);
            }
        }
    }

    private void saveProducts() {
        ArrayList<String> lines = new ArrayList<>();
        for (Product p : products) {
            lines.add(p.getId() + "," + p.getName() + "," + String.format("%.2f", p.getPrice()) + "," + p.getStock());
        }
        files.writeLines("products.csv", lines);
    }

    private void loadTransactions() {
        for (String line : files.readLines("transactions.csv")) {
            String[] parts = line.split(",");
            try {
                transactions.add(new Transaction(parts[0], parts[1], parts[2],
                        Double.parseDouble(parts[3]), Double.parseDouble(parts[4]), parts[5]));
            } catch (Exception e) {
                System.out.println("Skipped a bad transaction line: " + line);
            }
        }
    }

    private void saveTransactions() {
        ArrayList<String> lines = new ArrayList<>();
        for (Transaction t : transactions) {
            lines.add(t.getId() + "," + t.getDateTime() + "," + t.getCashier() + ","
                    + String.format("%.2f", t.getTotal()) + "," + String.format("%.2f", t.getCash()) + ","
                    + t.getItems());
        }
        files.writeLines("transactions.csv", lines);
    }
}
