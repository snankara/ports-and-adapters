package io.github.snankara.shop.adapter.out.persistence;

import io.github.snankara.shop.application.port.out.persistence.ProductRepository;
import io.github.snankara.shop.model.product.Product;
import io.github.snankara.shop.model.product.ProductId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractProductRepositoryTest<T extends ProductRepository>{
    private T productRepository;

    @BeforeEach
    void initRepository() {
        productRepository = createProductRepository();
    }

    protected abstract T createProductRepository();

    @Test
    void givenTestProductsAndATestProductId_findById_returnsATestProduct() {
        ProductId productId = DemoProducts.COMPUTER_MONITOR.id();

        Optional<Product> product = productRepository.findById(productId);

        assertThat(product).contains(DemoProducts.COMPUTER_MONITOR);
    }

    @Test
    void givenTheIdOfAProductNotPersisted_findById_returnsAnEmptyOptional() {
        ProductId productId = new ProductId("00000");

        Optional<Product> product = productRepository.findById(productId);

        assertThat(product).isEmpty();
    }

    @Test
    void
    givenTestProductsAndASearchQueryNotMatchingAndProduct_findByNameOrDescription_returnsAnEmptyList() {
        String query = "not matching name or description";

        List<Product> products = productRepository.findByNameOrDescription(query);

        assertThat(products).isEmpty();
    }

    @Test
    void
    givenTestProductsAndASearchQueryMatchingOneProduct_findByNameOrDescription_returnsThatProduct() {
        String query = "curved";

        List<Product> products = productRepository.findByNameOrDescription(query);

        assertThat(products).containsExactlyInAnyOrder(DemoProducts.COMPUTER_MONITOR);
    }

    @Test
    void
    givenTestProductsAndASearchQueryMatchingTwoProducts_findByNameOrDescription_returnsThoseProducts() {
        String query = "computer";

        List<Product> products = productRepository.findByNameOrDescription(query);

        assertThat(products)
                .containsExactlyInAnyOrder(DemoProducts.COMPUTER_MONITOR, DemoProducts.KEYBOARD);
    }
}
