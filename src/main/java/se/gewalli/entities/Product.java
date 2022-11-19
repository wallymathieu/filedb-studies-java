package se.gewalli.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Product {
    public final int id;
    public final float cost;
    public final String name;
    public final int version;
    @JsonCreator
    public Product(
        @JsonProperty("id") int id,
        @JsonProperty("cost") float cost,
        @JsonProperty("name") String name,
        @JsonProperty("version") int version) {
        this.id = id;
        this.cost = cost;
        this.name = name;
        this.version = version;
    }
}
