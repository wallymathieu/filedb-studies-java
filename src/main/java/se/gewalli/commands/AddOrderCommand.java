package se.gewalli.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.atlassian.fugue.Option;
import io.atlassian.fugue.Options;
import io.atlassian.fugue.Try;
import se.gewalli.data.Repository;
import se.gewalli.entities.Customer;
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
    public void handle(Repository repository) {
        Options.lift((Customer c) -> {
            repository.save(new Order(id,
                    c,
                    orderDate,
                    new ArrayList<>(),
                    version));
            return 0;
        }).apply(repository.tryGetCustomer(customer));
    }
}
