package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.OrderDao;
import org.yearup.models.Order;
import org.yearup.models.OrderLineItem;
import org.yearup.models.Product;
import org.yearup.models.Profile;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlOrderDao extends MySqlDaoBase implements OrderDao
{
    public MySqlOrderDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public Order createFromCart(int userId, Profile shippingProfile, ShoppingCart cart)
    {
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty())
            throw new IllegalArgumentException("Shopping cart is empty.");

        if (shippingProfile == null)
            throw new IllegalArgumentException("Profile not found.");


        if (isBlank(shippingProfile.getAddress()) || isBlank(shippingProfile.getCity()) ||
                isBlank(shippingProfile.getState()) || isBlank(shippingProfile.getZip()))
        {
            throw new IllegalArgumentException("Profile shipping address is incomplete.");
        }

        LocalDateTime now = LocalDateTime.now();
        BigDecimal shipping = BigDecimal.ZERO;

        String insertOrderSql = """
                INSERT INTO orders (user_id, date, address, city, state, zip, shipping_amount)
                VALUES (?, ?, ?, ?, ?, ?, ?);
                """;

        String insertLineSql = """
                INSERT INTO order_line_items (order_id, product_id, sales_price, quantity, discount)
                VALUES (?, ?, ?, ?, ?);
                """;

        String clearCartSql = "DELETE FROM shopping_cart WHERE user_id = ?;";

        Connection connection = null;
        try
        {
            connection = getConnection();
            connection.setAutoCommit(false);

            int orderId;
            try (PreparedStatement orderStmt = connection.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS))
            {
                orderStmt.setInt(1, userId);
                orderStmt.setTimestamp(2, Timestamp.valueOf(now));
                orderStmt.setString(3, shippingProfile.getAddress());
                orderStmt.setString(4, shippingProfile.getCity());
                orderStmt.setString(5, shippingProfile.getState());
                orderStmt.setString(6, shippingProfile.getZip());
                orderStmt.setBigDecimal(7, shipping);

                orderStmt.executeUpdate();

                try (ResultSet keys = orderStmt.getGeneratedKeys())
                {
                    if (!keys.next())
                        throw new SQLException("Failed to create order (no generated key)." );

                    orderId = keys.getInt(1);
                }
            }

            List<OrderLineItem> createdLineItems = new ArrayList<>();
            try (PreparedStatement lineStmt = connection.prepareStatement(insertLineSql, Statement.RETURN_GENERATED_KEYS))
            {
                for (ShoppingCartItem cartItem : cart.getItems().values())
                {
                    Product product = cartItem.getProduct();
                    if (product == null)
                        throw new SQLException("Cart item missing product.");

                    BigDecimal salesPrice = product.getPrice();
                    int qty = cartItem.getQuantity();
                    BigDecimal discount = cartItem.getDiscountPercent();

                    lineStmt.setInt(1, orderId);
                    lineStmt.setInt(2, product.getProductId());
                    lineStmt.setBigDecimal(3, salesPrice);
                    lineStmt.setInt(4, qty);
                    lineStmt.setBigDecimal(5, discount);

                    lineStmt.executeUpdate();

                    int orderLineItemId = 0;
                    try (ResultSet keys = lineStmt.getGeneratedKeys())
                    {
                        if (keys.next())
                            orderLineItemId = keys.getInt(1);
                    }

                    OrderLineItem oli = new OrderLineItem();
                    oli.setOrderLineItemId(orderLineItemId);
                    oli.setOrderId(orderId);
                    oli.setProductId(product.getProductId());
                    oli.setSalesPrice(salesPrice);
                    oli.setQuantity(qty);
                    oli.setDiscount(discount);
                    oli.setProduct(product);
                    createdLineItems.add(oli);
                }
            }

            try (PreparedStatement clearStmt = connection.prepareStatement(clearCartSql))
            {
                clearStmt.setInt(1, userId);
                clearStmt.executeUpdate();
            }

            connection.commit();

            Order order = new Order();
            order.setOrderId(orderId);
            order.setUserId(userId);
            order.setDate(now);
            order.setAddress(shippingProfile.getAddress());
            order.setCity(shippingProfile.getCity());
            order.setState(shippingProfile.getState());
            order.setZip(shippingProfile.getZip());
            order.setShippingAmount(shipping);
            order.setLineItems(createdLineItems);

            BigDecimal total = cart.getTotal().add(shipping);
            order.setOrderTotal(total);

            return order;
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                try { connection.rollback(); } catch (SQLException ignored) {}
            }

            throw (e instanceof RuntimeException) ? (RuntimeException) e : new RuntimeException(e);
        }
        finally
        {
            if (connection != null)
            {
                try { connection.close(); } catch (SQLException ignored) {}
            }
        }
    }

    private boolean isBlank(String s)
    {
        return s == null || s.trim().isEmpty();
    }
}

