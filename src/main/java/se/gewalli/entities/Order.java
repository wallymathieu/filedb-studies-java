package se.gewalli.entities;


import java.util.Collection;
import java.util.Collections;
import java.time.Instant;

public class Order {
    public final int id;
    public final Customer customer;
    public final Instant orderDate;
    public final Collection<Product> products;
    public final int version;

    public Order(int id, Customer customer, Instant orderDate, Collection<Product> products, int version) {
        this.id = id;
        this.customer = customer;
        this.orderDate = orderDate;
        this.products = Collections.unmodifiableCollection(products);
        this.version = version;
    }
}
