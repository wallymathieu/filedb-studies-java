package se.gewalli.commands

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import se.gewalli.data.EntityNotFound
import se.gewalli.data.Repository
import se.gewalli.entities.Order
import java.time.Instant

class AddOrderCommand @JsonCreator constructor(
    @JsonProperty("id") id: Int,
    @JsonProperty("version") version: Int,
    @param:JsonProperty("customer") val customer: Int,
    @param:JsonProperty("orderDate") val orderDate: Instant
) : Command(id, version) {
    override val type: CommandType get()= CommandType.AddOrderCommand

    @Throws(EntityNotFound::class)
    override fun run(repository: Repository) {
        repository.save(
            Order(
                id,
                repository.getCustomer(customer),
                orderDate,
                ArrayList(),
                version
            )
        )
    }
}