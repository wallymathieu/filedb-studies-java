package se.gewalli.commands

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import se.gewalli.data.Repository
import se.gewalli.entities.Customer

class AddCustomerCommand @JsonCreator constructor(
    @JsonProperty("id") id: Int,
    @JsonProperty("version") version: Int,
    @param:JsonProperty("firstname") val firstname: String,
    @param:JsonProperty("lastname") val lastname: String
) : Command(id, version) {
    override val type: CommandType get()= CommandType.AddCustomerCommand

    override fun run(repository: Repository) {
        repository.save(Customer(id, firstname, lastname, version))
    }
}