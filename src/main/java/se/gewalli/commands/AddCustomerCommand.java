package se.gewalli.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import se.gewalli.data.Repository;
import se.gewalli.entities.Customer;

public record AddCustomerCommand(@JsonProperty("id") int id,
                                 @JsonProperty("version") int version,
                                 @JsonProperty("firstname") String firstname,
                                 @JsonProperty("lastname") String lastname) implements Command {

    @Override
    public CommandType getType() {
        return CommandType.AddCustomerCommand;
    }

    @Override
    public void run(Repository repository) {
        repository.save(new Customer(id, firstname, lastname, version));
    }
}
