package se.gewalli.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import se.gewalli.data.EntityNotFound;
import se.gewalli.data.Repository;
import se.gewalli.entities.Order;

import java.time.Instant;
import java.util.ArrayList;

public final class AddOrderCommand extends Command {
    public final int customer;
    public final Instant orderDate;

    @JsonCreator
    public AddOrderCommand(@JsonProperty("id") int id,
                           @JsonProperty("version") int version,
                           @JsonProperty("customer") int customer,
                           @JsonProperty("orderDate") Instant orderDate) {
        super(id, version);
        this.customer = customer;
        this.orderDate = orderDate;
    }

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
