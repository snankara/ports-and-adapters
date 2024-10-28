package io.github.snankara.shop.adapter.in.rest.cart;

import io.github.snankara.shop.model.cart.CartLineItem;
import io.github.snankara.shop.model.money.Money;
import io.github.snankara.shop.model.product.Product;

public record CartLineItemModel(String productId, String productName, Money price, int quantity) {

    public static CartLineItemModel fromDomainModel(CartLineItem cartLineItem) {
        Product product = cartLineItem.product();
        return new CartLineItemModel(
                product.id().value(),
                product.name(),
                product.price(),
                cartLineItem.quantity());
    }
}
