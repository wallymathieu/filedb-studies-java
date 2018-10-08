import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.gewalli.commands.Command;
import se.gewalli.data.InMemoryRepository;
import se.gewalli.data.Repository;
import xmlimport.GetCommands;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomerDataTests {
    Repository repository;

    @BeforeEach
    public void beforeEach() {
        repository = new InMemoryRepository();
        GetCommands getCommands = new GetCommands();
        for (Command command : getCommands.Get()) {
            command.handle(repository);
        }
    }

    @Test
    public void canGetCustomerById() {
        assertEquals("Steve", repository.tryGetCustomer(1).get().firstname);
    }

    @Test
    public void canGetProductById() {
        assertEquals("Yo-yo", repository.tryGetProduct(1).get().name);
    }

    @Test
    public void orderContainsProduct() {
        assertEquals(3, repository.tryGetOrder(1).get().products.size());
    }
}
