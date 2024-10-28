package io.github.snankara.shop.application.service.cart;

import io.github.snankara.shop.application.port.out.persistence.CartRepository;
import io.github.snankara.shop.model.cart.Cart;
import io.github.snankara.shop.model.cart.NotEnoughItemsInStockException;
import io.github.snankara.shop.model.customer.CustomerId;
import io.github.snankara.shop.model.product.Product;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static io.github.snankara.shop.model.money.TestMoneyFactory.turkishLiras;
import static io.github.snankara.shop.model.product.TestProductFactory.createTestProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetCartServiceTest {
    private static final CustomerId TEST_CUSTOMER_ID = new CustomerId(1);
    private static final Product TEST_PRODUCT_1 = createTestProduct(turkishLiras(5,75));
    private static final Product TEST_PRODUCT_2 = createTestProduct(turkishLiras(7,45));

    private final CartRepository cartRepository = mock(CartRepository.class);
    private final GetCartService getCartService = new GetCartService(cartRepository);

    @Test
    void givenCartIsPersisted_getCart_returnsPersistedCart() throws NotEnoughItemsInStockException {
        Cart persistedCart = new Cart(TEST_CUSTOMER_ID);

        persistedCart.addProduct(TEST_PRODUCT_1, 2);
        persistedCart.addProduct(TEST_PRODUCT_2, 3);

        when(cartRepository.findByCustomerId(TEST_CUSTOMER_ID)).thenReturn(Optional.of(persistedCart));

        Cart cart = getCartService.getCart(TEST_CUSTOMER_ID);

        assertThat(cart).isSameAs(persistedCart);
    }

    @Test
    void givenCartIsNotPersisted_getCart_returnsAnEmptyCart() throws NotEnoughItemsInStockException {
        when(cartRepository.findByCustomerId(TEST_CUSTOMER_ID)).thenReturn(Optional.empty());

        Cart cart = getCartService.getCart(TEST_CUSTOMER_ID);
        assertThat(cart).isNotNull();
        assertThat(cart.lineItems()).isEmpty();
    }
}
