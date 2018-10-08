package se.gewalli.controllers;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import se.gewalli.CommandsHandler;
import se.gewalli.commands.AddOrderCommand;
import se.gewalli.commands.AddProductToOrderCommand;
import se.gewalli.commands.Command;
import se.gewalli.data.Repository;
import se.gewalli.entities.Order;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

@RestController()
public class OrdersController {
    public static class CreateOrder {
        public int id;
        public int customer;
    }

    public static class AddProduct {
        public int productId;
    }

    @Autowired
    private Repository repository;
    @Autowired
    private CommandsHandler persistCommandsHandler;

    @RequestMapping(value = "/api/orders/{id}", method = RequestMethod.GET)
    public ResponseEntity<Order> get(int id) {
        return repository.tryGetOrder(id).map(ResponseEntity::ok)
                .getOrElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @RequestMapping(value = "/api/orders", method = RequestMethod.GET)
    public ResponseEntity<Order[]> get() {
        return ResponseEntity.ok(repository.getOrders().toArray(new Order[0]));
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = Order.class)})
    @RequestMapping(value = "/api/orders", method = RequestMethod.POST)
    public CompletableFuture<ResponseEntity<Order>> add(@RequestBody() CreateOrder body) {
        Command c = new AddOrderCommand(body.id, 0, body.customer, Instant.now());
        return persistCommandsHandler.handle(c).thenApply(res ->
                res.fold(err -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null),
                        a -> ResponseEntity.ok(repository.getOrder(body.id))));
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = Order.class)})
    @RequestMapping(value = "/api/orders/{id}/products", method = RequestMethod.POST)
    public CompletableFuture<ResponseEntity<Order>> addProduct(int id, @RequestBody() AddProduct body) {
        Command c = new AddProductToOrderCommand(0, 0, id, body.productId);
        return persistCommandsHandler.handle(c).thenApply(res ->
                res.fold(err -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null),
                        a -> ResponseEntity.ok(repository.getOrder(id))));
    }
}
