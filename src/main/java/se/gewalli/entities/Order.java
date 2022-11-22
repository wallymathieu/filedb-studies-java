package se.gewalli.entities;


import java.util.Collection;
import java.util.Collections;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record Order(
        @JsonProperty("id") int id,
        @JsonProperty("customer") Customer customer,
        @JsonProperty("orderDate") Instant orderDate,
        @JsonProperty("products") Collection<Product> products,
        @JsonProperty("version") int version) {
}
