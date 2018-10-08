package se.gewalli.data;

import io.atlassian.fugue.Option;
import se.gewalli.entities.Customer;
import se.gewalli.entities.Order;
import se.gewalli.entities.Product;

import java.util.Collection;

public abstract class Repository {
    public abstract Option<Customer> tryGetCustomer(int customerId);

    public abstract Option<Product> tryGetProduct(int productId);

    public abstract Option<Order> tryGetOrder(int orderId);

    public abstract void save(Product obj);

    public abstract void save(Order obj);

    public abstract void save(Customer obj);

    public Customer getCustomer(int customerId) {
        return tryGetCustomer(customerId)
                .getOrError(() -> String.format("Could not find customer %d", customerId));
    }

    public Product getProduct(int productId) {
        return tryGetProduct(productId)
                .getOrError(() -> String.format("Could not find product %d", productId));
    }

    public Order getOrder(int orderId) {
        return tryGetOrder(orderId)
                .getOrError(() -> String.format("Could not find order %d", orderId));
    }

    public abstract Collection<Customer> getCustomers();

    public abstract Collection<Product> getProducts();

    public abstract Collection<Order> getOrders();
}
