package se.gewalli.entities;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Collection;

public record Order(
        @JsonProperty("id") int id,
        @JsonProperty("customer") Customer customer,
        @JsonProperty("orderDate") Instant orderDate,
        @JsonProperty("products") Collection<Product> products,
        @JsonProperty("version") int version) {
}
