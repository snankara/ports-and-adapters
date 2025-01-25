package io.github.snankara.shop.adapter.out.persistence;

import io.github.snankara.shop.application.port.out.persistence.CartRepository;
import io.github.snankara.shop.application.port.out.persistence.ProductRepository;
import io.github.snankara.shop.model.cart.Cart;
import io.github.snankara.shop.model.cart.CartLineItem;
import io.github.snankara.shop.model.cart.NotEnoughItemsInStockException;
import io.github.snankara.shop.model.customer.CustomerId;
import io.github.snankara.shop.model.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static io.github.snankara.shop.model.money.TestMoneyFactory.turkishLiras;
import static io.github.snankara.shop.model.product.TestProductFactory.createTestProduct;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractCartRepositoryTest<T extends CartRepository, U extends ProductRepository>{

    private static final Product TEST_PRODUCT_1 = createTestProduct(turkishLiras(19, 99));
    private static final Product TEST_PRODUCT_2 = createTestProduct(turkishLiras(1, 49));

    private static final AtomicInteger CUSTOMER_ID_SEQUENCE_GENERATOR = new AtomicInteger();

    private T cartRepository;

    @BeforeEach
    void initRepositories() {
        cartRepository = createCartRepository();
        persistTestProducts();
    }

    protected abstract T createCartRepository();

    private void persistTestProducts() {
        U productRepository = createProductRepository();
        productRepository.add(TEST_PRODUCT_1);
        productRepository.add(TEST_PRODUCT_2);
    }

    protected abstract U createProductRepository();

    @Test
    void givenACustomerIdForWhichNoCartIsPersisted_findByCustomerId_returnsAnEmptyOptional() {
        CustomerId customerId = createUniqueCustomerId();

        Optional<Cart> cart = cartRepository.findByCustomerId(customerId);

        assertThat(cart).isEmpty();
    }

    @Test
    void givenPersistedCartWithProduct_findByCustomerId_returnsTheAppropriateCart()
            throws NotEnoughItemsInStockException {
        CustomerId customerId = createUniqueCustomerId();

        Cart persistedCart = new Cart(customerId);
        persistedCart.addProduct(TEST_PRODUCT_1, 1);
        cartRepository.add(persistedCart);

        Optional<Cart> cart = cartRepository.findByCustomerId(customerId);

        assertThat(cart).isNotEmpty();
        assertThat(cart.get().customerId()).isEqualTo(customerId);
        assertThat(cart.get().lineItems()).hasSize(1);
        assertThat(cart.get().lineItems().getFirst().product()).isEqualTo(TEST_PRODUCT_1);
        assertThat(cart.get().lineItems().getFirst().quantity()).isEqualTo(1);
    }

    @Test
    void
    givenExistingCartWithProduct_andGivenANewCartForTheSameCustomer_saveCart_overwritesTheExistingCart()
            throws NotEnoughItemsInStockException {
        CustomerId customerId = createUniqueCustomerId();

        Cart existingCart = new Cart(customerId);
        existingCart.addProduct(TEST_PRODUCT_1, 1);
        cartRepository.add(existingCart);

        //overwrites
        Cart newCart = new Cart(customerId);
        newCart.addProduct(TEST_PRODUCT_2, 2);
        cartRepository.add(newCart);

        //getting new cart
        Optional<Cart> cart = cartRepository.findByCustomerId(customerId);
        assertThat(cart).isNotEmpty();
        assertThat(cart.get().customerId()).isEqualTo(customerId);
        assertThat(cart.get().lineItems()).hasSize(1);
        assertThat(cart.get().lineItems().getFirst().product()).isEqualTo(TEST_PRODUCT_2);
        assertThat(cart.get().lineItems().getFirst().quantity()).isEqualTo(2);
    }

    @Test
    void givenExistingCartWithProduct_addProductAndSaveCart_updatesTheExistingCart()
            throws NotEnoughItemsInStockException {
        CustomerId customerId = createUniqueCustomerId();

        Cart existingCart = new Cart(customerId);
        existingCart.addProduct(TEST_PRODUCT_1, 1);
        cartRepository.add(existingCart);

        existingCart = cartRepository.findByCustomerId(customerId).orElseThrow();
        existingCart.addProduct(TEST_PRODUCT_2, 2);
        cartRepository.add(existingCart);

        Optional<Cart> cart = cartRepository.findByCustomerId(customerId);
        assertThat(cart).isNotEmpty();
        assertThat(cart.get().customerId()).isEqualTo(customerId);
        assertThat(cart.get().lineItems())
                .map(CartLineItem::product)
                .containsExactlyInAnyOrder(TEST_PRODUCT_1, TEST_PRODUCT_2);
    }

    @Test
    void givenExistingCart_deleteByCustomerId_deletesTheCart() {
        CustomerId customerId = createUniqueCustomerId();

        Cart existingCart = new Cart(customerId);
        cartRepository.add(existingCart);

        assertThat(cartRepository.findByCustomerId(customerId)).isNotEmpty();

        cartRepository.deleteByCustomerId(customerId);

        assertThat(cartRepository.findByCustomerId(customerId)).isEmpty();
    }

    @Test
    void givenNotExistingCart_deleteByCustomerId_doesNothing() {
        CustomerId customerId = createUniqueCustomerId();
        assertThat(cartRepository.findByCustomerId(customerId)).isEmpty();

        cartRepository.deleteByCustomerId(customerId);

        assertThat(cartRepository.findByCustomerId(customerId)).isEmpty();
    }


    private static CustomerId createUniqueCustomerId() {
        return new CustomerId(CUSTOMER_ID_SEQUENCE_GENERATOR.incrementAndGet());
    }
}
