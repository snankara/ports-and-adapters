package io.github.snankara.shop.adapter.in.rest.product;

import io.github.snankara.shop.model.money.Money;
import io.github.snankara.shop.model.product.Product;

public record ProductInListModel(String id, String name, Money price, int itemsInStock) {

    public static ProductInListModel fromDomainModel(Product product) {
        return new ProductInListModel(
                product.id().value(),
                product.name(),
                product.price(),
                product.stock());
    }
}
