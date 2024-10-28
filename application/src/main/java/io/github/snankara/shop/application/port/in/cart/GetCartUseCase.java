package io.github.snankara.shop.application.port.in.cart;

import io.github.snankara.shop.model.cart.Cart;
import io.github.snankara.shop.model.customer.CustomerId;

public interface GetCartUseCase {
    Cart getCart(CustomerId customerId);
}
