package io.github.snankara.shop.application.service.cart;

import io.github.snankara.shop.application.port.out.persistence.CartRepository;
import io.github.snankara.shop.model.customer.CustomerId;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class EmptyCartServiceTest {
    private static final CustomerId TEST_CUSTOMER_ID = new CustomerId(1);

    private final CartRepository cartRepository = mock(CartRepository.class);
    private final EmptyCartService emptyCartService = new EmptyCartService(cartRepository);

    @Test
    void emptyCart_invokesDeleteOnThePersistencePort(){
        emptyCartService.emptyCart(TEST_CUSTOMER_ID);
        verify(cartRepository).deleteByCustomerId(TEST_CUSTOMER_ID);
    }
}
