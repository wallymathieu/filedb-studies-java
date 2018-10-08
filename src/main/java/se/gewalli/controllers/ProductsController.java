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
import se.gewalli.commands.AddProductCommand;
import se.gewalli.commands.Command;
import se.gewalli.data.Repository;
import se.gewalli.entities.Product;

import java.util.concurrent.CompletableFuture;

@RestController()
public class ProductsController {
    public static class CreateProduct {
        public int id;
        public float cost;
        public String name;
    }

    @Autowired
    private Repository repository;
    @Autowired
    private CommandsHandler persistCommandsHandler;

    @RequestMapping(value = "/api/products/{id}", method = RequestMethod.GET)
    public ResponseEntity<Product> get(int id) {
        return repository.tryGetProduct(id).map(ResponseEntity::ok)
                .getOrElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @RequestMapping(value = "/api/products", method = RequestMethod.GET)
    public ResponseEntity<Product[]> get() {
        return ResponseEntity.ok(repository.getProducts().toArray(new Product[0]));
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = Product.class)})
    @RequestMapping(value = "/api/products", method = RequestMethod.POST)
    public CompletableFuture<ResponseEntity<Product>> add(@RequestBody() CreateProduct body) {
        Command c = new AddProductCommand(body.id, 0, body.cost, body.name);
        return persistCommandsHandler.handle(c).thenApply(res ->
                res.fold(err -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null),
                        a -> ResponseEntity.ok(repository.getProduct(body.id))));
    }
}
