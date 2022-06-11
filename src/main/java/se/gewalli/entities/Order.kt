package se.gewalli.entities

import java.time.Instant
import java.util.*

class Order(val id: Int, val customer: Customer, val orderDate: Instant, products: Collection<Product>?, version: Int) {
    @JvmField
    val products: Collection<Product>
    val version: Int

    init {
        this.products = Collections.unmodifiableCollection(products)
        this.version = version
    }
}