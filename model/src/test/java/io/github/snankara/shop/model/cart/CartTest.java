package io.github.snankara.shop.model.cart;

import io.github.snankara.shop.model.product.Product;
import io.github.snankara.shop.model.product.TestProductFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;

import static io.github.snankara.shop.model.cart.TestCartFactory.emptyCartForRandomCustomer;
import static io.github.snankara.shop.model.money.TestMoneyFactory.turkishLiras;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;

class CartTest {

    @Test
    void givenEmptyCart_addTwoProducts_numberOfItemsAndSubTotalIsCalculatedCorrectly() throws NotEnoughItemsInStockException{

        Cart cart = emptyCartForRandomCustomer();

        Product product1 = TestProductFactory.createTestProduct(turkishLiras(10, 25));
        Product product2 = TestProductFactory.createTestProduct(turkishLiras(8, 99));

        cart.addProduct(product1, 3);
        cart.addProduct(product2, 4);

        assertThat(cart.numberOfItems()).isEqualTo(7);
        assertThat(cart.subTotal()).isEqualTo(turkishLiras(66,71));
    }

    @Test
    void givenEmptyCart_addTwoProducts_productsAreInCart() throws NotEnoughItemsInStockException {
        Cart cart = emptyCartForRandomCustomer();

        Product product1 = TestProductFactory.createTestProduct(turkishLiras(10, 25));
        Product product2 = TestProductFactory.createTestProduct(turkishLiras(8, 99));

        cart.addProduct(product1, 3);
        cart.addProduct(product2, 4);

        assertThat(cart.lineItems()).hasSize(2);

        assertThat(cart.lineItems().get(0).product()).isEqualTo(product1);
        assertThat(cart.lineItems().get(0).quantity()).isEqualTo(3);

        assertThat(cart.lineItems().get(1).product()).isEqualTo(product2);
        assertThat(cart.lineItems().get(1).quantity()).isEqualTo(4);

    }

    @Test
    void givenAProductWithAFewItemsAvailable_addMoreItemsThanAvailableToTheCart_throwsException() {
        Cart cart = emptyCartForRandomCustomer();

        Product product = TestProductFactory.createTestProduct(turkishLiras(14, 50), 3);

        ThrowingCallable invocation = () -> cart.addProduct(product, 4);

        assertThatExceptionOfType(NotEnoughItemsInStockException.class)
                .isThrownBy(invocation)
                .satisfies(ex -> assertThat(ex.itemsInStock()).isEqualTo(product.stock()));
    }

    @Test
    void givenAProductWithAFewItemsAvailable_addAllAvailableItemsToTheCart_succeeds() {
        Cart cart = emptyCartForRandomCustomer();
        Product product = TestProductFactory.createTestProduct(turkishLiras(5, 95), 3);

        ThrowingCallable invocation = () -> cart.addProduct(product, 3);

        assertThatNoException().isThrownBy(invocation);
    }

    @ParameterizedTest
    @ValueSource(ints = {-100, -1, 0})
    void givenEmptyCart_addLessThanOneItemOfAProduct_throwsException(int quantity) {
        Cart cart = emptyCartForRandomCustomer();
        Product product = TestProductFactory.createTestProduct(turkishLiras(1, 95));

        ThrowingCallable invocation = () -> cart.addProduct(product, quantity);

        assertThatIllegalArgumentException().isThrownBy(invocation);
    }
}
