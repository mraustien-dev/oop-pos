import java.util.ArrayList;

public class Cart {
    private ArrayList<CartItem> items = new ArrayList<>();

    public void addItem(Product product) {
        addItem(product, 1);
    }

    public void addItem(Product product, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be more than 0.");
        }

        CartItem existing = null;
        for (CartItem item : items) {
            if (item.getProduct() == product) {
                existing = item;
            }
        }

        int alreadyInCart = 0;
        if (existing != null) {
            alreadyInCart = existing.getQuantity();
        }
        if (alreadyInCart + quantity > product.getStock()) {
            throw new IllegalArgumentException("Only " + product.getStock() + " "
                    + product.getName() + " left in stock.");
        }

        if (existing != null) {
            existing.addQuantity(quantity);
        } else {
            items.add(new CartItem(product, quantity));
        }
    }

    public void removeItem(int index) {
        items.remove(index);
    }

    public void clear() {
        items.clear();
    }

    public double getTotal() {
        double total = 0;
        for (CartItem item : items) {
            total = total + item.getSubtotal();
        }
        return Math.round(total * 100) / 100.0;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public ArrayList<CartItem> getItems() {
        return items;
    }
}
