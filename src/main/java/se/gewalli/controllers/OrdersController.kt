package se.gewalli.controllers

import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import se.gewalli.CommandsHandler
import se.gewalli.FailureReason
import se.gewalli.commands.AddOrderCommand
import se.gewalli.commands.AddProductToOrderCommand
import se.gewalli.commands.Command
import se.gewalli.data.Repository
import se.gewalli.entities.Order
import se.gewalli.kyminon.Result
import java.time.Instant
import java.util.concurrent.CompletableFuture

@RestController
class OrdersController {
    class CreateOrder {
        var id = 0
        var customer = 0
    }

    class AddProduct {
        var productId = 0
    }

    @Autowired
    private val repository: Repository? = null

    @Autowired
    private val commandsHandler: CommandsHandler? = null

    @RequestMapping(value = ["/api/orders/{id}"], method = [RequestMethod.GET])
    operator fun get(@PathVariable id: Int): ResponseEntity<Order?> {
        return repository!!.tryGetOrder(id).map { body: Order? -> ResponseEntity.ok(body) }
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null as Order?))
    }

    @RequestMapping(value = ["/api/orders"], method = [RequestMethod.GET])
    fun get(): ResponseEntity<Array<Order>> {
        return ResponseEntity.ok(repository!!.orders.toTypedArray())
    }

    @ApiResponses(value = [ApiResponse(code = 200, message = "successful operation", response = Order::class)])
    @RequestMapping(value = ["/api/orders"], method = [RequestMethod.POST])
    fun add(@RequestBody body: CreateOrder): CompletableFuture<ResponseEntity<Order?>> {
        val command: Command = AddOrderCommand(body.id, 0, body.customer, Instant.now())
        return commandsHandler!!.handle(command).thenApply { result: Result<Int, FailureReason> ->
            result.fold(
                {
                    ResponseEntity.ok(
                        repository!!.tryGetOrder(body.id).orElse(null as Order?)
                    )
                }
            ) { ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null as Order?) }
        }
    }

    @ApiResponses(value = [ApiResponse(code = 200, message = "successful operation", response = Order::class)])
    @RequestMapping(value = ["/api/orders/{id}/products"], method = [RequestMethod.POST])
    fun addProduct(id: Int, @RequestBody body: AddProduct): CompletableFuture<ResponseEntity<Order?>> {
        val command: Command = AddProductToOrderCommand(0, 0, id, body.productId)
        return commandsHandler!!.handle(command).thenApply { result: Result<Int, FailureReason> ->
            result.fold(
                {
                    ResponseEntity.ok(
                        repository!!.tryGetOrder(id).orElse(null as Order?)
                    )
                }
            ) { ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null as Order?) }
        }
    }
}