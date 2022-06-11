package se.gewalli.controllers

import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import se.gewalli.CommandsHandler
import se.gewalli.FailureReason
import se.gewalli.commands.AddProductCommand
import se.gewalli.commands.Command
import se.gewalli.data.Repository
import se.gewalli.entities.Product
import se.gewalli.kyminon.Result
import java.util.concurrent.CompletableFuture

@RestController
class ProductsController {
    class CreateProduct {
        var id = 0
        var cost = 0f
        var name: String? = null
    }

    @Autowired
    private val repository: Repository? = null

    @Autowired
    private val commandsHandler: CommandsHandler? = null

    @RequestMapping(value = ["/api/products/{id}"], method = [RequestMethod.GET])
    operator fun get(@PathVariable id: Int): ResponseEntity<Product?> {
        return repository!!.tryGetProduct(id).map { body: Product? -> ResponseEntity.ok(body) }
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null as Product?))
    }

    @RequestMapping(value = ["/api/products"], method = [RequestMethod.GET])
    fun get(): ResponseEntity<Array<Product>> {
        return ResponseEntity.ok(repository!!.products.toTypedArray())
    }

    @ApiResponses(value = [ApiResponse(code = 200, message = "successful operation", response = Product::class)])
    @RequestMapping(value = ["/api/products"], method = [RequestMethod.POST])
    fun add(@RequestBody body: CreateProduct): CompletableFuture<ResponseEntity<Product?>> {
        val command: Command = AddProductCommand(body.id, 0, body.cost, body.name!!)
        return commandsHandler!!.handle(command).thenApply { result: Result<Int, FailureReason> ->
            result.fold(
                {
                    ResponseEntity.ok(
                        repository!!.tryGetProduct(body.id).orElse(null as Product?)
                    )
                }
            ) { ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null as Product?) }
        }
    }
}