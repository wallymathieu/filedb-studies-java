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
        assertEquals("Steve", repository.getCustomer(1).firstname);
    }

    @Test
    public void canGetProductById() {
        assertEquals("Yo-yo", repository.getProduct(1).name);
    }

    @Test
    public void orderContainsProduct() {
        assertEquals(3, repository.getOrder(1).products.size());
    }
}
