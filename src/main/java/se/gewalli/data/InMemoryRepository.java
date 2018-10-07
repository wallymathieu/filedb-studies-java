package se.gewalli.data;

import se.gewalli.entities.Customer;
import se.gewalli.entities.Order;
import se.gewalli.entities.Product;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepository extends Repository {
    Map<Integer, Customer> customerMap = new HashMap<>();
    Map<Integer, Product> productMap = new HashMap<>();
    Map<Integer, Order> orderMap = new HashMap<>();

    @Override
    public Optional<Customer> tryGetCustomer(int customerId) {
        return Optional.ofNullable(customerMap.get(customerId));
    }

    @Override
    public Optional<Product> tryGetProduct(int productId) {
        return Optional.ofNullable(productMap.get(productId));
    }

    @Override
    public Optional<Order> tryGetOrder(int orderId) {
        return Optional.ofNullable(orderMap.get(orderId));
    }

    @Override
    public void save(Product obj) {
        productMap.put(obj.id, obj);
    }

    @Override
    public void save(Order obj) {
        orderMap.put(obj.id, obj);
    }

    @Override
    public void save(Customer obj) {
        customerMap.put(obj.id, obj);
    }

    @Override
    public Collection<Customer> getCustomers() {
        return customerMap.values();
    }

    @Override
    public Collection<Product> getProducts() {
        return productMap.values();
    }

    @Override
    public Collection<Order> getOrders() {
        return orderMap.values();
    }

}
