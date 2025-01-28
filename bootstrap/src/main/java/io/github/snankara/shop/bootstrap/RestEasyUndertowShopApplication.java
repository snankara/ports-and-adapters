package io.github.snankara.shop.bootstrap;

import io.github.snankara.shop.adapter.in.rest.cart.AddToCartController;
import io.github.snankara.shop.adapter.in.rest.cart.EmptyCartController;
import io.github.snankara.shop.adapter.in.rest.cart.GetCartController;
import io.github.snankara.shop.adapter.in.rest.product.FindProductsController;
import io.github.snankara.shop.adapter.out.persistence.inmemory.InMemoryCartRepository;
import io.github.snankara.shop.adapter.out.persistence.inmemory.InMemoryProductRepository;
import io.github.snankara.shop.application.port.in.cart.AddToCartUseCase;
import io.github.snankara.shop.application.port.in.cart.EmptyCartUseCase;
import io.github.snankara.shop.application.port.in.cart.GetCartUseCase;
import io.github.snankara.shop.application.port.in.product.FindProductsUseCase;
import io.github.snankara.shop.application.port.out.persistence.CartRepository;
import io.github.snankara.shop.application.port.out.persistence.ProductRepository;
import io.github.snankara.shop.application.service.cart.AddToCartService;
import io.github.snankara.shop.application.service.cart.EmptyCartService;
import io.github.snankara.shop.application.service.cart.GetCartService;
import io.github.snankara.shop.application.service.product.FindProductsService;
import jakarta.ws.rs.core.Application;

import java.util.Set;

public class RestEasyUndertowShopApplication extends Application {
    private ProductRepository productRepository;
    private CartRepository cartRepository;

    @Override
    public Set<Object> getSingletons() {
        initPersistenceAdapters();

        return Set.of(
          addToCartController(),
          getCartController(),
          emptyCartController(),
          findProductsController()
        );
    }

    private void initPersistenceAdapters(){
        this.productRepository = new InMemoryProductRepository();
        this.cartRepository = new InMemoryCartRepository();
    }

    private AddToCartController addToCartController(){
        AddToCartUseCase addToCartUseCase = new AddToCartService(this.cartRepository, this.productRepository);
        return new AddToCartController(addToCartUseCase);
    }

    private GetCartController getCartController(){
        GetCartUseCase getCartUseCase = new GetCartService(this.cartRepository);
        return new GetCartController(getCartUseCase);
    }

    private EmptyCartController emptyCartController(){
        EmptyCartUseCase emptyCartUseCase = new EmptyCartService(this.cartRepository);
        return new EmptyCartController(emptyCartUseCase);
    }

    private FindProductsController findProductsController(){
        FindProductsUseCase findProductsUseCase = new FindProductsService(this.productRepository);
        return new FindProductsController(findProductsUseCase);
    }
}
