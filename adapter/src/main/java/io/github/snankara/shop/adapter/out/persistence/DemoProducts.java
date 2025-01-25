package io.github.snankara.shop.adapter.out.persistence;

import io.github.snankara.shop.model.money.Money;
import io.github.snankara.shop.model.product.Product;
import io.github.snankara.shop.model.product.ProductId;

import java.util.Currency;
import java.util.List;

public final class DemoProducts {
    private static final Currency TRY = Currency.getInstance("TRY");

    public static final Product COMPUTER_MONITOR =
            new Product(
                    new ProductId(ProductId.randomProductId().value()),
                    "32-Inch Curved Computer Monitor",
                    "",
                    Money.of(TRY, 159, 95),
                    1_041);


    public static final Product KEYBOARD =
            new Product(
                    new ProductId(ProductId.randomProductId().value()),
                    "Soft Computer Keyboard Minimal",
                    "",
                    Money.of(TRY, 55, 45),
                    150);

    public static final Product CELLPHONE =
            new Product(
                    new ProductId(ProductId.randomProductId().value()),
                    "256 GB Phone",
                    "",
                    Money.of(TRY, 449, 95),
                    30);

    public static final List<Product> DEMO_PRODUCTS =
            List.of(COMPUTER_MONITOR, KEYBOARD, CELLPHONE);

}
