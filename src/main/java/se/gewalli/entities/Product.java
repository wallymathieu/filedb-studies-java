package se.gewalli.entities;

public class Product {
    public final int id;
    public final float cost;
    public final String name;
    public final int version;

    public Product(int id, float cost, String name, int version) {
        this.id = id;
        this.cost = cost;
        this.name = name;
        this.version = version;
    }
}
