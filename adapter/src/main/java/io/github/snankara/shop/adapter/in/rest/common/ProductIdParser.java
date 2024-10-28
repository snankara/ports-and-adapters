package io.github.snankara.shop.adapter.in.rest.common;

import io.github.snankara.shop.model.product.ProductId;
import jakarta.ws.rs.core.Response;

import java.util.Objects;

import static io.github.snankara.shop.adapter.in.rest.common.CommonsController.clientErrorException;

public final class ProductIdParser {
    private ProductIdParser() {}

    public static ProductId parse(String productId) {
        if (Objects.isNull(productId))
            throw clientErrorException(Response.Status.BAD_REQUEST, "Missing 'productId' value.");

        try {
            return new ProductId(productId);
        }catch (IllegalArgumentException e) {
            throw clientErrorException(Response.Status.BAD_REQUEST, "Invalid 'productId' value.");
        }
    }

}
