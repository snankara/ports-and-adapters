package io.github.snankara.shop.application.service.product;

import io.github.snankara.shop.application.port.in.product.FindProductsUseCase;
import io.github.snankara.shop.application.port.out.persistence.ProductRepository;
import io.github.snankara.shop.model.product.Product;

import java.util.List;
import java.util.Objects;

public class FindProductsService implements FindProductsUseCase {

    private final ProductRepository productRepository;
    public FindProductsService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findByNameOrDescription(String query) {
        Objects.requireNonNull(query, "'query' must not be null");

        if (query.length() < 2)
            throw new IllegalArgumentException("'query' must contain at least two characters");

        return this.productRepository.findByNameOrDescription(query);
    }
}
