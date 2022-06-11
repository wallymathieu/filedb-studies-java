package se.gewalli.data

import se.gewalli.data.EntityNotFound
import se.gewalli.entities.Customer
import se.gewalli.entities.Order
import se.gewalli.entities.Product
import java.util.*

abstract class Repository {
    abstract fun tryGetCustomer(customerId: Int): Optional<Customer>
    abstract fun tryGetProduct(productId: Int): Optional<Product>
    abstract fun tryGetOrder(orderId: Int): Optional<Order>
    abstract fun save(product: Product)
    abstract fun save(order: Order)
    abstract fun save(customer: Customer)
    @Throws(EntityNotFound::class)
    fun getCustomer(customerId: Int): Customer {
        return tryGetCustomer(customerId)
            .orElseThrow { EntityNotFound(String.format("Could not find customer %d", customerId)) }
    }

    @Throws(EntityNotFound::class)
    fun getProduct(productId: Int): Product {
        return tryGetProduct(productId)
            .orElseThrow { EntityNotFound(String.format("Could not find product %d", productId)) }
    }

    @Throws(EntityNotFound::class)
    fun getOrder(orderId: Int): Order {
        return tryGetOrder(orderId)
            .orElseThrow { EntityNotFound(String.format("Could not find order %d", orderId)) }
    }

    abstract val customers: Collection<Customer>
    abstract val products: Collection<Product>
    abstract val orders: Collection<Order>
}