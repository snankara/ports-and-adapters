package io.github.snankara.shop.adapter.in.rest.cart;

import io.github.snankara.shop.model.cart.Cart;
import io.github.snankara.shop.model.money.Money;

import java.util.List;

public record CartModel(List<CartLineItemModel> lineItems, int numberOfItems, Money subTotal) {

    public static CartModel fromDomainModel(Cart cart){
        return new CartModel(
                cart.lineItems().stream().map(CartLineItemModel::fromDomainModel).toList(),
                cart.numberOfItems(),
                cart.subTotal());
    }
}
