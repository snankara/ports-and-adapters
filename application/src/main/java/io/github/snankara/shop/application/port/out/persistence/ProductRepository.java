package io.github.snankara.shop.application.port.out.persistence;

import io.github.snankara.shop.model.product.Product;
import io.github.snankara.shop.model.product.ProductId;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> findByNameOrDescription(String query);
    void add(Product product);
    Optional<Product> findById(ProductId id);
}
