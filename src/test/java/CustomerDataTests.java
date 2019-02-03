import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.gewalli.commands.Command;
import se.gewalli.data.EntityNotFound;
import se.gewalli.data.InMemoryRepository;
import se.gewalli.data.Repository;
import xmlimport.GetCommands;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomerDataTests {
    Repository repository;
    @BeforeEach
    public void beforeEach() throws EntityNotFound {
        repository=new InMemoryRepository();
        var getCommands=new GetCommands();
        for (Command command : getCommands.Get()) {
            command.run(repository);
        }
    }
    @Test
    public void canGetCustomerById() throws EntityNotFound {
        assertEquals("Steve" , repository.getCustomer(1).firstname);
    }
    @Test
    public void canGetProductById() throws EntityNotFound {
        assertEquals("Yo-yo" , repository.getProduct(1).name);
    }
    @Test
    public void orderContainsProduct() throws EntityNotFound {
        assertEquals(3 , repository.getOrder(1).products.size());
    }
}
