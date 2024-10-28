package io.github.snankara.shop.adapter.in.rest.product;

import io.github.snankara.shop.application.port.in.product.FindProductsUseCase;
import io.github.snankara.shop.model.product.Product;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

import static io.github.snankara.shop.adapter.in.rest.common.CommonsController.clientErrorException;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
public class FindProductsController {
    private final FindProductsUseCase findProductsUseCase;

    public FindProductsController(FindProductsUseCase findProductsUseCase) {
        this.findProductsUseCase = findProductsUseCase;
    }

    @GET
    public List<ProductInListModel> findProducts(@QueryParam("query") String query){
        if (query == null) throw clientErrorException(Response.Status.BAD_REQUEST, "Missing 'query'");

        try {
            List<Product> products = findProductsUseCase.findByNameOrDescription(query);
            return products.stream().map(ProductInListModel::fromDomainModel).toList();
        } catch (IllegalArgumentException e) {
            throw clientErrorException(Response.Status.BAD_REQUEST, "Invalid 'query'");
        }
    }
}
