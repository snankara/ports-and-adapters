package io.github.snankara.shop.adapter.out.persistence.inmemory;

import io.github.snankara.shop.application.port.out.persistence.CartRepository;
import io.github.snankara.shop.model.cart.Cart;
import io.github.snankara.shop.model.customer.CustomerId;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryCartRepository implements CartRepository {

    private final Map<CustomerId, Cart> carts = new ConcurrentHashMap<>();

    @Override
    public void add(Cart cart) {
        carts.put(cart.customerId(), cart);
    }

    @Override
    public Optional<Cart> findByCustomerId(CustomerId customerId) {
        return Optional.ofNullable(carts.get(customerId));
    }

    @Override
    public void deleteByCustomerId(CustomerId customerId) {
        carts.remove(customerId);
    }
}
