package io.github.snankara.shop.model.customer;

public record CustomerId(int value) {
    public CustomerId {
        if (value < 1) throw new IllegalArgumentException("Value must be positive");
    }
}
