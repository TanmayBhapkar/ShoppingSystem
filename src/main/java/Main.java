import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner input = new Scanner(System.in);
    private static final ProductDAO productDAO = new ProductDAO();
    private static final OrderDAO orderDAO = new OrderDAO();
    private static final List<CartItem> cart = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("\n===== ONLINE SHOPPING SYSTEM =====");
        boolean running = true;
        while (running) {
            System.out.println("\n1. View products\n2. Add to cart\n3. View cart\n4. Checkout\n5. Exit");
            System.out.print("Select option: ");
            int choice = readInt();
            try {
                switch (choice) {
                    case 1 -> showProducts();
                    case 2 -> addToCart();
                    case 3 -> showCart();
                    case 4 -> checkout();
                    case 5 -> running = false;
                    default -> System.out.println("Please select 1 to 5.");
                }
            } catch (SQLException ex) {
                System.out.println("Database error: " + ex.getMessage());
                System.out.println("Check that MySQL is running and DB credentials are correct.");
            }
        }
        System.out.println("Thank you!");
    }

    private static int readInt() {
        while (!input.hasNextInt()) {
            System.out.print("Enter a valid number: ");
            input.next();
        }
        int n = input.nextInt();
        input.nextLine();
        return n;
    }

    private static void showProducts() throws SQLException {
        System.out.println("\nID | PRODUCT            | PRICE       | STOCK");
        for (Product p : productDAO.getAllProducts()) System.out.println(p);
    }

    private static void addToCart() throws SQLException {
        showProducts();
        System.out.print("Product ID: ");
        int id = readInt();
        Product product = productDAO.findById(id);
        if (product == null) {
            System.out.println("Product not found.");
            return;
        }
        System.out.print("Quantity: ");
        int quantity = readInt();
        int alreadyInCart = cart.stream()
                .filter(i -> i.getProduct().getId() == id)
                .mapToInt(CartItem::getQuantity).sum();
        if (quantity <= 0 || alreadyInCart + quantity > product.getStock()) {
            System.out.println("Invalid quantity or insufficient stock.");
            return;
        }
        for (CartItem item : cart) {
            if (item.getProduct().getId() == id) {
                item.addQuantity(quantity);
                System.out.println("Cart updated.");
                return;
            }
        }
        cart.add(new CartItem(product, quantity));
        System.out.println("Added to cart.");
    }

    private static void showCart() {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }
        double total = 0;
        for (CartItem item : cart) {
            System.out.printf("%s x %d = Rs. %.2f%n", item.getProduct().getName(),
                    item.getQuantity(), item.getSubtotal());
            total += item.getSubtotal();
        }
        System.out.printf("Total: Rs. %.2f%n", total);
    }

    private static void checkout() throws SQLException {
        orderDAO.placeOrder(cart);
        cart.clear();
    }
}
