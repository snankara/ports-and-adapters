package io.github.snankara.shop.adapter.in.rest.common;

import io.github.snankara.shop.model.customer.CustomerId;
import jakarta.ws.rs.core.Response;

import java.util.Objects;

import static io.github.snankara.shop.adapter.in.rest.common.CommonsController.clientErrorException;

public final class CustomerIdParser {

    private CustomerIdParser() {}

    public static CustomerId parse(String customerId) {
        if (Objects.isNull(customerId))
            throw clientErrorException(Response.Status.BAD_REQUEST, "Missing 'customerId' value.");

        try {
            return new CustomerId(Integer.parseInt(customerId));
        }catch (IllegalArgumentException ex) {
            throw clientErrorException(Response.Status.BAD_REQUEST, "Invalid 'customerId' value.");
        }
    }
}
