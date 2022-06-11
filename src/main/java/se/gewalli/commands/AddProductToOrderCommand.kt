package se.gewalli.commands

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import se.gewalli.data.EntityNotFound
import se.gewalli.data.Repository
import se.gewalli.entities.Order

class AddProductToOrderCommand @JsonCreator constructor(
    @JsonProperty("id") id: Int,
    @JsonProperty("version") version: Int,
    @param:JsonProperty("orderId") val orderId: Int,
    @param:JsonProperty("productId") val productId: Int
) : Command(id, version) {
    override val type: CommandType get()= CommandType.AddProductToOrderCommand

    @Throws(EntityNotFound::class)
    override fun run(repository: Repository) {
        val order = repository.getOrder(orderId)
        val productList = ArrayList(order.products)
        productList.add(repository.getProduct(productId))
        repository.save(Order(order.id, order.customer, order.orderDate, productList, order.version + 1))
    }
}