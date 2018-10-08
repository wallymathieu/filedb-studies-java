package se.gewalli.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.atlassian.fugue.Option;
import io.atlassian.fugue.Options;
import se.gewalli.data.Repository;
import se.gewalli.entities.Order;
import se.gewalli.entities.Product;

import java.util.ArrayList;
import java.util.List;

public final class AddProductToOrderCommand extends Command {
    public final int orderId;
    public final int productId;

    @JsonCreator
    public AddProductToOrderCommand(@JsonProperty("id") int id,
                                    @JsonProperty("version") int version,
                                    @JsonProperty("orderId") int orderId,
                                    @JsonProperty("productId") int productId) {
        super(id, version);
        this.orderId = orderId;
        this.productId = productId;
    }

    @Override
    public CommandType getType() {
        return CommandType.AddProductToOrderCommand;
    }

    @Override
    public void handle(Repository repository) {
        Options.lift2((Order order, Product product) -> {
            ArrayList<Product> productList = new ArrayList<>(order.products);
            productList.add(product);
            repository.save(new Order(order.id, order.customer, order.orderDate, productList, order.version + 1));
            return 0;
        }).apply(repository.tryGetOrder(orderId), repository.tryGetProduct(productId));
    }
}
