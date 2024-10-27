package io.github.snankara.shop.application.port.in.cart;

import io.github.snankara.shop.model.customer.CustomerId;

public interface EmptyCartUseCase {
    void emptyCart(CustomerId customerId);
}
