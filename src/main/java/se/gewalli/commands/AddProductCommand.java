package se.gewalli.commands;

import com.fasterxml.jackson.annotation.*;
import se.gewalli.data.Repository;
import se.gewalli.entities.Product;

public final class AddProductCommand extends Command {
    public final float cost;
    public final String name;

    @JsonCreator
    public AddProductCommand(@JsonProperty("id") int id,
                             @JsonProperty("version") int version,
                             @JsonProperty("cost") float cost,
                             @JsonProperty("name") String name) {
        super(id, version);
        this.cost = cost;
        this.name = name;
    }

    @Override
    public CommandType getType() {
        return CommandType.AddProductCommand;
    }

    @Override
    public void run(Repository repository) {
        repository.save(new Product(id, cost, name, version));
    }
}
