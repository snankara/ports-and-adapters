package io.github.snankara.shop.application.service.cart;

import io.github.snankara.shop.application.port.in.cart.GetCartUseCase;
import io.github.snankara.shop.application.port.out.persistence.CartRepository;
import io.github.snankara.shop.model.cart.Cart;
import io.github.snankara.shop.model.customer.CustomerId;

public class GetCartService implements GetCartUseCase {
    private final CartRepository cartRepository;

    public GetCartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public Cart getCart(CustomerId customerId) {
        return this.cartRepository
                .findByCustomerId(customerId)
                .orElseGet(() -> new Cart(customerId));
    }
}
