package se.gewalli.controllers

import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import se.gewalli.CommandsHandler
import se.gewalli.FailureReason
import se.gewalli.commands.AddCustomerCommand
import se.gewalli.commands.Command
import se.gewalli.data.Repository
import se.gewalli.entities.Customer
import se.gewalli.kyminon.Result
import java.util.concurrent.CompletableFuture

@RestController
class CustomersController {
    class CreateCustomer {
        var id = 0
        var firstname: String? = null
        var lastname: String? = null
    }

    @Autowired
    private val repository: Repository? = null

    @Autowired
    private val commandsHandler: CommandsHandler? = null

    @RequestMapping(value = ["/api/customers/{id}"], method = [RequestMethod.GET])
    operator fun get(@PathVariable id: Int): ResponseEntity<Customer?> {
        return repository!!.tryGetCustomer(id).map { body: Customer? -> ResponseEntity.ok(body) }
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null as Customer?))
    }

    @RequestMapping(value = ["/api/customers"], method = [RequestMethod.GET])
    fun get(): ResponseEntity<Array<Customer>> {
        return ResponseEntity.ok(repository!!.customers.toTypedArray())
    }

    @ApiResponses(value = [ApiResponse(code = 200, message = "successful operation", response = Customer::class)])
    @RequestMapping(value = ["/api/customers"], method = [RequestMethod.POST])
    fun add(@RequestBody body: CreateCustomer): CompletableFuture<ResponseEntity<Customer?>> {
        val command: Command = AddCustomerCommand(body.id, 0, body.firstname!!, body.lastname!!)
        return commandsHandler!!.handle(command).thenApply { result: Result<Int, FailureReason> ->
            result.fold(
                {
                    ResponseEntity.ok(
                        repository!!.tryGetCustomer(body.id).orElse(null as Customer?)
                    )
                }
            ) { ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null as Customer?) }
        }
    }
}