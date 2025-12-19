package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.OrderDao;
import org.yearup.data.ProfileDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.Order;
import org.yearup.models.Profile;
import org.yearup.models.ShoppingCart;

import java.security.Principal;


@RestController
@RequestMapping("/orders")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class OrdersController
{
    private final OrderDao orderDao;
    private final ShoppingCartDao shoppingCartDao;
    private final ProfileDao profileDao;
    private final UserDao userDao;

    public OrdersController(OrderDao orderDao, ShoppingCartDao shoppingCartDao, ProfileDao profileDao, UserDao userDao)
    {
        this.orderDao = orderDao;
        this.shoppingCartDao = shoppingCartDao;
        this.profileDao = profileDao;
        this.userDao = userDao;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order createOrder(Principal principal)
    {
        try
        {
            int userId = getUserId(principal);

            ShoppingCart cart = shoppingCartDao.getByUserId(userId);
            if (cart == null || cart.getItems() == null || cart.getItems().isEmpty())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shopping cart is empty.");

            Profile profile = profileDao.getByUserId(userId);
            if (profile == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Profile not found.");

            return orderDao.createFromCart(userId, profile, cart);
        }
        catch (ResponseStatusException ex)
        {
            throw ex;
        }
        catch (IllegalArgumentException ex)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        catch (Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    private int getUserId(Principal principal)
    {
        if (principal == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        String username = principal.getName();
        int userId = userDao.getIdByUsername(username);

        if (userId <= 0)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        return userId;
    }
}