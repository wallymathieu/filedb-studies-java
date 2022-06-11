package se.gewalli.commands

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.As
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id
import se.gewalli.data.EntityNotFound
import se.gewalli.data.Repository

@JsonTypeInfo(include = As.PROPERTY, use = Id.NAME, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = AddCustomerCommand::class),
    JsonSubTypes.Type(value = AddOrderCommand::class),
    JsonSubTypes.Type(value = AddProductCommand::class),
    JsonSubTypes.Type(value = AddProductToOrderCommand::class)
)
abstract class Command(val id: Int, val version: Int) {
    abstract val type: CommandType?
    @Throws(EntityNotFound::class)
    abstract fun run(repository: Repository)
}