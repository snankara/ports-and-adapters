package io.github.snankara.shop.application.port.out.persistence;

import io.github.snankara.shop.model.cart.Cart;
import io.github.snankara.shop.model.customer.CustomerId;

import java.util.Optional;

public interface CartRepository {
    void add(Cart cart);
    Optional<Cart> findByCustomerId(CustomerId customerId);
}
