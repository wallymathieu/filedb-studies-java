package se.gewalli.entities;


import java.util.Collection;
import java.util.Collections;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class Order {
    public final int id;
    public final Customer customer;
    public final Instant orderDate;
    public final Collection<Product> products;
    public final int version;
    @JsonCreator
    public Order(
        @JsonProperty("id") int id,
        @JsonProperty("customer") Customer customer,
        @JsonProperty("orderDate") Instant orderDate,
        @JsonProperty("products") Collection<Product> products,
        @JsonProperty("version") int version) {
        this.id = id;
        this.customer = customer;
        this.orderDate = orderDate;
        this.products = Collections.unmodifiableCollection(products);
        this.version = version;
    }
}
