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

    @Override
    public ShoppingCart addProduct(int userId, int productId) {
        String sql = """
                INSERT INTO shopping_cart (user_id, product_id, quantity)
                VALUES (?, ?, 1)
                ON DUPLICATE KEY UPDATE quantity = quantity + 1;
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, productId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return getByUserId(userId);
    }

    @Override
    public ShoppingCart updateQuantity(int userId, int productId, int quantity) {
        if (quantity <= 0) {
            String deleteSql = "DELETE FROM shopping_cart WHERE user_id = ? AND product_id = ?;";
            try (Connection connection = getConnection();
                 PreparedStatement statement = connection.prepareStatement(deleteSql)) {
                statement.setInt(1, userId);
                statement.setInt(2, productId);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            return getByUserId(userId);
        }
        String sql = """
                UPDATE shopping_cart
                SET quantity = ?
                WHERE user_id = ? AND product_id = ?;
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, quantity);
            statement.setInt(2, userId);
            statement.setInt(3, productId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return getByUserId(userId);
    }
    @Override
    public void clearCart(int userId)
    {
        String sql = "DELETE FROM shopping_cart WHERE user_id = ?;";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1, userId);
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
    private Product mapProduct(ResultSet row) throws SQLException
    {
        Product p = new Product();
        p.setProductId(row.getInt("product_id"));
        p.setName(row.getString("name"));
        p.setPrice(row.getBigDecimal("price"));
        p.setCategoryId(row.getInt("category_id"));
        p.setDescription(row.getString("description"));
        p.setSubCategory(row.getString("subcategory"));
        p.setStock(row.getInt("stock"));
        p.setFeatured(row.getBoolean("featured"));
        p.setImageUrl(row.getString("image_url"));
        return p;
    }
}



