import java.sql.*;
import java.util.List;

public class OrderDAO {
    public void placeOrder(List<CartItem> items) throws SQLException {
        if (items.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        String insertOrder = "INSERT INTO orders (total_amount) VALUES (?)";
        String reduceStock = "UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?";
        String insertItem = "INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                double total = items.stream().mapToDouble(CartItem::getSubtotal).sum();
                int orderId;

                try (PreparedStatement ps = con.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setDouble(1, total);
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) throw new SQLException("Could not create order.");
                        orderId = keys.getInt(1);
                    }
                }

                for (CartItem item : items) {
                    try (PreparedStatement ps = con.prepareStatement(reduceStock)) {
                        ps.setInt(1, item.getQuantity());
                        ps.setInt(2, item.getProduct().getId());
                        ps.setInt(3, item.getQuantity());
                        if (ps.executeUpdate() != 1) {
                            throw new SQLException("Not enough stock for " + item.getProduct().getName());
                        }
                    }
                    try (PreparedStatement ps = con.prepareStatement(insertItem)) {
                        ps.setInt(1, orderId);
                        ps.setInt(2, item.getProduct().getId());
                        ps.setInt(3, item.getQuantity());
                        ps.setDouble(4, item.getProduct().getPrice());
                        ps.executeUpdate();
                    }
                }
                con.commit();
                System.out.printf("Order #%d placed. Total: Rs. %.2f%n", orderId, total);
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }
}
