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
import se.gewalli.PersistCommandsHandler;
import se.gewalli.commands.AddCustomerCommand;
import se.gewalli.commands.Command;
import se.gewalli.data.Repository;
import se.gewalli.entities.Customer;

import java.util.concurrent.CompletableFuture;

@RestController()
public class CustomersController {
    public static class CreateCustomer{
        public int id;
        public String firstname;
        public String lastname;
    }
    @Autowired
    private Repository repository;
    @Autowired
    private PersistCommandsHandler persistCommandsHandler;
    @RequestMapping(value = "/api/customers/{id}", method = RequestMethod.GET)
    public ResponseEntity<Customer> get(int id) {
        return repository.tryGetCustomer(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }
    @RequestMapping(value = "/api/customers", method = RequestMethod.GET)
    public ResponseEntity<Customer[]> get() {
        return ResponseEntity.ok(repository.getCustomers().toArray(new Customer[0]));
    }
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = Customer.class)})
    @RequestMapping(value = "/api/customers", method = RequestMethod.POST)
    public CompletableFuture<ResponseEntity<Customer>> add(@RequestBody()CreateCustomer body) {
        Command c=new AddCustomerCommand(body.id,0, body.firstname, body.lastname);
        return persistCommandsHandler.handle(c).thenApply(res->
                res.map(a -> ResponseEntity.ok(repository.tryGetCustomer(body.id).orElse(null)),
                        err->ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)));
    }
}
