package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import java.security.Principal;

@RestController
@RequestMapping("/cart")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class ShoppingCartController
{
    private final ShoppingCartDao shoppingCartDao;
    private final UserDao userDao;
    private final ProductDao productDao;

    public ShoppingCartController(ShoppingCartDao shoppingCartDao,UserDao userDao,ProductDao productDao){
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    @GetMapping
    public ShoppingCart getCart(Principal principal)
    {
        try
        {
            int userId = getUserId(principal);
            ShoppingCart cart = shoppingCartDao.getByUserId(userId);
            return (cart == null) ? new ShoppingCart() : cart;
        }
        catch (ResponseStatusException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
    @PostMapping("/products/{productId}")
    public ShoppingCart addProduct(@PathVariable int productId, Principal principal)
    {
        try
        {
            int userId = getUserId(principal);

            Product product = productDao.getById(productId);
            if (product == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found.");

            return shoppingCartDao.addProduct(userId, productId);
        }
        catch (ResponseStatusException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
    @PutMapping("/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateQuantity(@PathVariable int productId,
                               @RequestBody ShoppingCartItem item,
                               Principal principal) {
        try {
            int userId = getUserId(principal);

            if (item == null || item.getQuantity() <= 0)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be > 0.");


            boolean updated = shoppingCartDao.updateQuantity(userId, productId, item.getQuantity());
            if (!updated)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found in cart.");


        }
        catch (ResponseStatusException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
    @DeleteMapping
    public ShoppingCart clearCart(Principal principal)
    {
        try
        {
            int userId = getUserId(principal);
            shoppingCartDao.clearCart(userId);

            return new ShoppingCart();
        }
        catch (ResponseStatusException ex)
        {
            throw ex;
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
