package io.github.snankara.shop.adapter.in.rest.cart;

import io.github.snankara.shop.adapter.in.rest.common.CustomerIdParser;
import io.github.snankara.shop.adapter.in.rest.common.ProductIdParser;
import io.github.snankara.shop.application.port.in.cart.AddToCartUseCase;
import io.github.snankara.shop.application.port.in.cart.ProductNotFoundException;
import io.github.snankara.shop.model.cart.Cart;
import io.github.snankara.shop.model.cart.NotEnoughItemsInStockException;
import io.github.snankara.shop.model.customer.CustomerId;
import io.github.snankara.shop.model.product.ProductId;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import static io.github.snankara.shop.adapter.in.rest.common.CommonsController.clientErrorException;

@Path("/carts")
@Produces(MediaType.APPLICATION_JSON)
public class AddToCartController {
    private final AddToCartUseCase addToCartUseCase;

    public AddToCartController(AddToCartUseCase addToCartUseCase) {
        this.addToCartUseCase = addToCartUseCase;
    }

    @POST
    @Path("/{customerId}/line-items")
    public CartModel addLineItem(@PathParam("customerId") String customerId,
                                 @QueryParam("productId") String productId,
                                 @QueryParam("quantity") int quantity){

        CustomerId parsedCustomerId = CustomerIdParser.parse(customerId);
        ProductId parsedProductId = ProductIdParser.parse(productId);

        try {
            Cart cart = addToCartUseCase.addToCart(parsedProductId, parsedCustomerId, quantity);
            return CartModel.fromDomainModel(cart);
        } catch (NotEnoughItemsInStockException e) {
            throw clientErrorException(Response.Status.BAD_REQUEST, "Only %d items in stock".formatted(e.itemsInStock()));
        } catch (ProductNotFoundException e) {
            throw clientErrorException(Response.Status.BAD_REQUEST, "Product with id %s not found".formatted(productId));
        }
    }
}
