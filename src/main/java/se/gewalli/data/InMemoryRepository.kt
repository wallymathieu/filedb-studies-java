package se.gewalli.data

import se.gewalli.entities.Customer
import se.gewalli.entities.Order
import se.gewalli.entities.Product
import java.util.*

class InMemoryRepository : Repository() {
    private var customerMap = HashMap<Int, Customer>()
    private var productMap = HashMap<Int, Product>()
    private var orderMap = HashMap<Int, Order>()
    override fun tryGetCustomer(customerId: Int): Optional<Customer> {
        return Optional.ofNullable(customerMap[customerId])
    }

    override fun tryGetProduct(productId: Int): Optional<Product> {
        return Optional.ofNullable(productMap[productId])
    }

    override fun tryGetOrder(orderId: Int): Optional<Order> {
        return Optional.ofNullable(orderMap[orderId])
    }

    override fun save(product: Product) {
        productMap[product.id] = product
    }

    override fun save(order: Order) {
        orderMap[order.id] = order
    }

    override fun save(customer: Customer) {
        customerMap[customer.id] = customer
    }

    override val customers: Collection<Customer> get() = customerMap.values

    override val products: Collection<Product> get()= productMap.values

    override val orders: Collection<Order> get()=orderMap.values
}