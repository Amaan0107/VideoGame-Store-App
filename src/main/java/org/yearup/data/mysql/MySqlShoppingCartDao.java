package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.*;


@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {
    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        ShoppingCart cart = new ShoppingCart();

        String sql = """
                SELECT sc.product_id, sc.quantity,
                       p.product_id, p.name, p.price, p.category_id, p.description, p.subcategory,
                       p.stock, p.featured, p.image_url
                FROM shopping_cart sc
                JOIN products p ON p.product_id = sc.product_id
                WHERE sc.user_id = ?;
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);

            try (ResultSet row = statement.executeQuery()) {
                while (row.next()) {
                    int productId = row.getInt("product_id");
                    int quantity = row.getInt("quantity");

                    Product product = mapProduct(row);

                    ShoppingCartItem item = new ShoppingCartItem();
                    item.setProduct(product);
                    item.setQuantity(quantity);
                    item.setDiscountPercent(java.math.BigDecimal.ZERO);

                    cart.getItems().put(productId, item);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return cart;
    }
}

