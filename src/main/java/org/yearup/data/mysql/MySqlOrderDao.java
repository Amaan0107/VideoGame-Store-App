package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.OrderDao;
import org.yearup.models.Order;
import org.yearup.models.Profile;
import org.yearup.models.ShoppingCart;


import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDateTime;


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
