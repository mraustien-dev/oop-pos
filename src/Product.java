// ENCAPSULATION: all fields are private.
// The only way to change them is through methods that CHECK the values first.
public class Product {
    private String id;
    private String name;
    private double price;
    private int stock;

    public Product(String id, String name, double price, int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }
        this.id = id;
        this.stock = stock;
        update(name, price);
    }

    // Change name and price together, only if both are valid.
    public void update(String name, double price) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name is required.");
        }
        if (name.contains(",") || name.contains(";")) {
            throw new IllegalArgumentException("Product name cannot contain , or ;");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be more than 0.");
        }
        this.name = name.trim();
        this.price = price;
    }

    public void addStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be more than 0.");
        }
        stock = stock + quantity;
    }

    public void removeStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be more than 0.");
        }
        if (quantity > stock) {
            throw new IllegalArgumentException("Only " + stock + " " + name + " left in stock.");
        }
        stock = stock - quantity;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }
}
