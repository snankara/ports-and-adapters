package io.github.snankara.shop.application.port.in.cart;

import io.github.snankara.shop.model.cart.Cart;
import io.github.snankara.shop.model.cart.NotEnoughItemsInStockException;
import io.github.snankara.shop.model.customer.CustomerId;
import io.github.snankara.shop.model.product.ProductId;

public interface AddToCartUseCase {
    Cart addToCart(ProductId productId, CustomerId customerId, int quantity)
            throws NotEnoughItemsInStockException, ProductNotFoundException;
}
