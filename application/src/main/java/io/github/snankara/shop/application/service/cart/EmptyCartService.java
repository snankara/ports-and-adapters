package io.github.snankara.shop.application.service.cart;

import io.github.snankara.shop.application.port.in.cart.EmptyCartUseCase;
import io.github.snankara.shop.application.port.out.persistence.CartRepository;
import io.github.snankara.shop.model.customer.CustomerId;

import java.util.Objects;

public class EmptyCartService implements EmptyCartUseCase {

    private final CartRepository cartRepository;

    public EmptyCartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public void emptyCart(CustomerId customerId) {
        Objects.requireNonNull(customerId, "customerId cannot be null");
        this.cartRepository.deleteByCustomerId(customerId);
    }
}
