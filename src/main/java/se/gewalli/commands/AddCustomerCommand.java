package se.gewalli.commands;

import com.fasterxml.jackson.annotation.*;
import se.gewalli.data.Repository;
import se.gewalli.entities.Customer;

public final class AddCustomerCommand extends Command {
    public final String firstname;
    public final String lastname;

    @JsonCreator
    public AddCustomerCommand(@JsonProperty("id") int id,
                              @JsonProperty("version") int version,
                              @JsonProperty("firstname") String firstname,
                              @JsonProperty("lastname") String lastname) {
        super(id, version);
        this.firstname = firstname;
        this.lastname = lastname;
    }

    @Override
    public CommandType getType() {
        return CommandType.AddCustomerCommand;
    }

    @Override
    public void run(Repository repository) {
        repository.save(new Customer(id, firstname, lastname, version));
    }
}
