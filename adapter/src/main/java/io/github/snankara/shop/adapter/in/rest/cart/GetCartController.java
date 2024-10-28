package io.github.snankara.shop.adapter.in.rest.cart;

import io.github.snankara.shop.adapter.in.rest.common.CustomerIdParser;
import io.github.snankara.shop.application.port.in.cart.GetCartUseCase;
import io.github.snankara.shop.model.cart.Cart;
import io.github.snankara.shop.model.customer.CustomerId;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/carts")
@Produces(MediaType.APPLICATION_JSON)
public class GetCartController {

    private final GetCartUseCase getCartUseCase;

    public GetCartController(GetCartUseCase getCartUseCase) {
        this.getCartUseCase = getCartUseCase;
    }

    @GET
    @Path("/{customerId}")
    public CartModel getCart(@PathParam("customerId") String customerId) {
        CustomerId parsedCustomerId = CustomerIdParser.parse(customerId);
        Cart cart = getCartUseCase.getCart(parsedCustomerId);
        return CartModel.fromDomainModel(cart);
    }
}
