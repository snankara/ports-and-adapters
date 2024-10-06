package io.github.snankara.shop.model.cart;

import io.github.snankara.shop.model.money.Money;
import io.github.snankara.shop.model.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
@RequiredArgsConstructor
public class CartLineItem {
    private final Product product;
    private int quantity;

    public void increaseQuantityBy(int augend, int itemsInStock)
            throws NotEnoughItemsInStockException {

        if (augend < 1) throw new IllegalArgumentException("You must add at least one item");

        int newQuantity = quantity + augend;
        if (itemsInStock < newQuantity) {
            throw new NotEnoughItemsInStockException(
                    ("Product %s has less items in stock (%d) "
                            + "than the requested total quantity (%d)")
                            .formatted(product.id(), product.stock(), newQuantity),
                    product.stock());
        }

        this.quantity = newQuantity;
    }

    public Money subTotal() {
        return product.price().multiply(quantity);
    }
}
