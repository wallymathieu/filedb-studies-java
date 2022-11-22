package se.gewalli.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import se.gewalli.data.EntityNotFound;
import se.gewalli.data.Repository;
import se.gewalli.entities.Order;

import java.time.Instant;
import java.util.ArrayList;

public record AddOrderCommand(@JsonProperty("id") int id,
                              @JsonProperty("version") int version,
                              @JsonProperty("customer") int customer,
                              @JsonProperty("orderDate") Instant orderDate) implements Command {
    @Override
    public CommandType getType() {
        return CommandType.AddOrderCommand;
    }

    @Override
    public void run(Repository repository) throws EntityNotFound {
        repository.save(new Order(id,
                repository.getCustomer(customer),
                orderDate,
                new ArrayList<>(),
                version));
    }
}
