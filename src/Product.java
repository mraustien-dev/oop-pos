public class Product {
    private String id;
    private String name;
    private String category;
    private double price;
    private int stock;
    private int reorderLevel;

    public Product(String id, String name, String category, double price, int stock, int reorderLevel) {
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }
        if (reorderLevel < 0) {
            throw new IllegalArgumentException("Reorder level cannot be negative.");
        }
        if (category == null || category.trim().isEmpty() || category.contains(",")) {
            category = "Other";
        }
        this.id = id;
        this.category = category.trim();
        this.stock = stock;
        this.reorderLevel = reorderLevel;
        update(name, price);
    }

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

    public String getCategory() {
        return category;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }
}
