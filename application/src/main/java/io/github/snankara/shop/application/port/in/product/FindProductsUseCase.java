package io.github.snankara.shop.application.port.in.product;

import io.github.snankara.shop.model.product.Product;

import java.util.List;

public interface FindProductsUseCase {
    List<Product> findByNameOrDescription(String query);
}
