package io.github.snankara.shop.application.service.product;

import io.github.snankara.shop.application.port.out.persistence.ProductRepository;
import io.github.snankara.shop.model.product.Product;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.github.snankara.shop.model.money.TestMoneyFactory.turkishLiras;
import static io.github.snankara.shop.model.product.TestProductFactory.createTestProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.*;

class FindProductsServiceTest {
    private static final Product TEST_PRODUCT_1 = createTestProduct(turkishLiras(15, 95));
    private static final Product TEST_PRODUCT_2 = createTestProduct(turkishLiras(12, 75));

    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final FindProductsService findProductsService = new FindProductsService(productRepository);

    @Test
    void givenASearchQuery_findByNameOrDescription_returnsTheProductsReturnedByThePersistencePort(){
        when(productRepository.findByNameOrDescription("product-one")).thenReturn(List.of(TEST_PRODUCT_1));
        when(productRepository.findByNameOrDescription("product-two")).thenReturn(List.of(TEST_PRODUCT_2));
        when(productRepository.findByNameOrDescription("product-one-two")).thenReturn(List.of(TEST_PRODUCT_1, TEST_PRODUCT_2));
        when(productRepository.findByNameOrDescription("product-three")).thenReturn(List.of());

        assertThat(findProductsService.findByNameOrDescription("product-one")).containsExactly(TEST_PRODUCT_1);
        assertThat(findProductsService.findByNameOrDescription("product-two")).containsExactly(TEST_PRODUCT_2);
        assertThat(findProductsService.findByNameOrDescription("product-one-two")).containsExactly(TEST_PRODUCT_1, TEST_PRODUCT_2);
        assertThat(findProductsService.findByNameOrDescription("product-three")).isEmpty();
    }

    @Test
    void givenATooShortSearchQuery_findByNameOrDescription_throwsAnException(){
        String query = "x";

        ThrowableAssert.ThrowingCallable invocation = () -> findProductsService.findByNameOrDescription(query);

        assertThatIllegalArgumentException().isThrownBy(invocation);
        verify(productRepository, never()).findByNameOrDescription(query);
    }
}
