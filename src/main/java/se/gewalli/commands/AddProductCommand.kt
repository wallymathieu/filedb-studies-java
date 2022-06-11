package se.gewalli.commands

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import se.gewalli.data.Repository
import se.gewalli.entities.Product

class AddProductCommand @JsonCreator constructor(
    @JsonProperty("id") id: Int,
    @JsonProperty("version") version: Int,
    @param:JsonProperty("cost") val cost: Float,
    @param:JsonProperty("name") val name: String
) : Command(id, version) {
    override val type: CommandType get()= CommandType.AddProductCommand;

    override fun run(repository: Repository) {
        repository.save(Product(id, cost, name, version))
    }
}