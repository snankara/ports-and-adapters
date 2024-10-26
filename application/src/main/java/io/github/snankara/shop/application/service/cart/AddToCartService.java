package io.github.snankara.shop.application.service.cart;

import io.github.snankara.shop.application.port.in.cart.AddToCartUseCase;
import io.github.snankara.shop.application.port.in.cart.ProductNotFoundException;
import io.github.snankara.shop.application.port.out.persistence.CartRepository;
import io.github.snankara.shop.application.port.out.persistence.ProductRepository;
import io.github.snankara.shop.model.cart.Cart;
import io.github.snankara.shop.model.cart.NotEnoughItemsInStockException;
import io.github.snankara.shop.model.customer.CustomerId;
import io.github.snankara.shop.model.product.Product;
import io.github.snankara.shop.model.product.ProductId;

import java.util.Objects;

public class AddToCartService implements AddToCartUseCase {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public AddToCartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }
    @Override
    public Cart addToCart(ProductId productId, CustomerId customerId, int quantity) throws NotEnoughItemsInStockException, ProductNotFoundException {
        Objects.requireNonNull(customerId, "CustomerId cannot be null");
        Objects.requireNonNull(productId, "ProductId cannot be null");

        if (quantity < 1) throw new IllegalArgumentException("Quantity cannot be less than 0");

        Product productToBeAddToCart = this.productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        Cart cart = this.cartRepository.findByCustomerId(customerId).orElseGet(() -> new Cart(customerId));

        cart.addProduct(productToBeAddToCart, quantity);
        this.cartRepository.add(cart);

        return cart;
    }
}
