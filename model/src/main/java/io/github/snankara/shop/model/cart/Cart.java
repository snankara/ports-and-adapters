package io.github.snankara.shop.model.cart;

import io.github.snankara.shop.model.customer.CustomerId;
import io.github.snankara.shop.model.money.Money;
import io.github.snankara.shop.model.product.Product;
import io.github.snankara.shop.model.product.ProductId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Accessors(fluent = true)
@RequiredArgsConstructor
public class Cart {

    @Getter
    private final CustomerId customerId;

    private final Map<ProductId, CartLineItem> lineItems = new LinkedHashMap<>();

    public void addProduct(Product product, int quantity)
            throws NotEnoughItemsInStockException {
        lineItems
                .computeIfAbsent(product.id(), ignored -> new CartLineItem(product))
                .increaseQuantityBy(quantity, product.stock());
    }

    public List<CartLineItem> lineItems() {
        return List.copyOf(lineItems.values());
    }

    public int numberOfItems() {
        return lineItems.values().stream().mapToInt(CartLineItem::quantity).sum();
    }

    public Money subTotal() {
        return lineItems.values().stream()
                .map(CartLineItem::subTotal)
                .reduce(Money::add)
                .orElse(null);
    }
}
