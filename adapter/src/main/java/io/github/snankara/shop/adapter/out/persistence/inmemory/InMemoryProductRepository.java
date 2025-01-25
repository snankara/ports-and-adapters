package io.github.snankara.shop.adapter.out.persistence.inmemory;

import io.github.snankara.shop.adapter.out.persistence.DemoProducts;
import io.github.snankara.shop.application.port.out.persistence.ProductRepository;
import io.github.snankara.shop.model.product.Product;
import io.github.snankara.shop.model.product.ProductId;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryProductRepository implements ProductRepository {

    private final Map<ProductId, Product> products = new ConcurrentHashMap<>();

    public InMemoryProductRepository() {
        createDemoProducts();
    }

    @Override
    public List<Product> findByNameOrDescription(String query) {
        String queryLowerCase = query.toLowerCase(Locale.ROOT);
        return products.values().stream().filter(product -> matchesQuery(product, queryLowerCase)).toList();
    }

    @Override
    public void add(Product product) {
        products.put(product.id(), product);
    }

    @Override
    public Optional<Product> findById(ProductId id) {
        return Optional.ofNullable(products.get(id));
    }

    private boolean matchesQuery(Product product, String query) {
        return product.name().toLowerCase(Locale.ROOT).contains(query)
                || product.description().toLowerCase(Locale.ROOT).contains(query);
    }

    private void createDemoProducts(){
        DemoProducts.DEMO_PRODUCTS.forEach(this::add);
    }
}
