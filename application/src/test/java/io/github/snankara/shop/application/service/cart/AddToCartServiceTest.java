package io.github.snankara.shop.application.service.cart;

import io.github.snankara.shop.application.port.in.cart.ProductNotFoundException;
import io.github.snankara.shop.application.port.out.persistence.CartRepository;
import io.github.snankara.shop.application.port.out.persistence.ProductRepository;
import io.github.snankara.shop.model.cart.Cart;
import io.github.snankara.shop.model.cart.NotEnoughItemsInStockException;
import io.github.snankara.shop.model.customer.CustomerId;
import io.github.snankara.shop.model.product.Product;
import io.github.snankara.shop.model.product.ProductId;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static io.github.snankara.shop.model.product.TestProductFactory.createTestProduct;
import static io.github.snankara.shop.model.money.TestMoneyFactory.turkishLiras;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


class AddToCartServiceTest {
    private static final CustomerId TEST_CUSTOMER_ID = new CustomerId(1);

    private static final Product TEST_PRODUCT_1 = createTestProduct(turkishLiras(10,95));
    private static final Product TEST_PRODUCT_2 = createTestProduct(turkishLiras(5,75));

    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);

    private final AddToCartService addToCartService = new AddToCartService(cartRepository, productRepository);

    @BeforeEach
    void initTestDoubles(){
        when(productRepository.findById(TEST_PRODUCT_1.id())).thenReturn(Optional.of(TEST_PRODUCT_1));
        when(productRepository.findById(TEST_PRODUCT_2.id())).thenReturn(Optional.of(TEST_PRODUCT_2));
    }

    @Test
    void givenExistingCart_addToCart_cartWithAddedProductIsSavedAndReturned() throws NotEnoughItemsInStockException, ProductNotFoundException {
        Cart persistedCart = new Cart(TEST_CUSTOMER_ID);
        persistedCart.addProduct(TEST_PRODUCT_1, 1);

        when(cartRepository.findByCustomerId(TEST_CUSTOMER_ID)).thenReturn(Optional.of(persistedCart));

        Cart cart = addToCartService.addToCart(TEST_PRODUCT_2.id(), TEST_CUSTOMER_ID, 4);
        verify(cartRepository).add(cart);

        assertThat(cart.lineItems()).hasSize(2);
        assertThat(cart.lineItems().get(0).product()).isEqualTo(TEST_PRODUCT_1);
        assertThat(cart.lineItems().get(0).quantity()).isEqualTo(1);
        assertThat(cart.lineItems().get(1).product()).isEqualTo(TEST_PRODUCT_2);
        assertThat(cart.lineItems().get(1).quantity()).isEqualTo(4);
    }

    @Test
    void givenNoExistingCart_addToCart_cartWithAddedProductIsSavedAndReturned() throws NotEnoughItemsInStockException, ProductNotFoundException {
        Cart cart = addToCartService.addToCart(TEST_PRODUCT_1.id(), TEST_CUSTOMER_ID, 5);

        verify(cartRepository).add(cart);

        assertThat(cart.lineItems()).hasSize(1);
        assertThat(cart.lineItems().getFirst().product()).isEqualTo(TEST_PRODUCT_1);
        assertThat(cart.lineItems().getFirst().quantity()).isEqualTo(5);
    }

    @Test
    void givenAnUnknownProductId_addToCart_throwsException() throws ProductNotFoundException {
        ProductId productId = ProductId.randomProductId();

        ThrowableAssert.ThrowingCallable invocation = () -> addToCartService.addToCart(productId, TEST_CUSTOMER_ID, 5);

        assertThatExceptionOfType(ProductNotFoundException.class).isThrownBy(invocation);
        verify(cartRepository, never()).add(any());
    }

    @Test
    void givenQuantityLessThan1_addToCart_throwsException() {
        ThrowableAssert.ThrowingCallable invocation = () -> addToCartService.addToCart(TEST_PRODUCT_1.id(), TEST_CUSTOMER_ID, 0);

        assertThatIllegalArgumentException().isThrownBy(invocation);
        verify(cartRepository, never()).add(any());
    }
}
