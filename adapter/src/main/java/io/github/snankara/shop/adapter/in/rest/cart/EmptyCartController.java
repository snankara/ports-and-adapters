package io.github.snankara.shop.adapter.in.rest.cart;

import io.github.snankara.shop.adapter.in.rest.common.CustomerIdParser;
import io.github.snankara.shop.application.port.in.cart.EmptyCartUseCase;
import io.github.snankara.shop.model.customer.CustomerId;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/carts")
@Produces(MediaType.APPLICATION_JSON)
public class EmptyCartController {
    private final EmptyCartUseCase emptyCartUseCase;

    public EmptyCartController(EmptyCartUseCase emptyCartUseCase) {
        this.emptyCartUseCase = emptyCartUseCase;
    }

    @DELETE
    @Path("/{customerId}")
    public void emptyCart(@PathParam("customerId") String customerId) {
        CustomerId parsedCustomerId = CustomerIdParser.parse(customerId);
        emptyCartUseCase.emptyCart(parsedCustomerId);
    }
}
