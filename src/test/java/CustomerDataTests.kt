import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import se.gewalli.data.EntityNotFound
import se.gewalli.data.InMemoryRepository
import se.gewalli.data.Repository
import xmlimport.GetCommands

class CustomerDataTests {
    var repository: Repository? = null
    @BeforeEach
    @Throws(EntityNotFound::class)
    fun beforeEach() {
        repository = InMemoryRepository()
        val getCommands = GetCommands()
        for (command in getCommands.Get()) {
            command.run(repository!!)
        }
    }

    @Test
    @Throws(EntityNotFound::class)
    fun canGetCustomerById() {
        Assertions.assertEquals("Steve", repository!!.getCustomer(1).firstname)
    }

    @Test
    @Throws(EntityNotFound::class)
    fun canGetProductById() {
        Assertions.assertEquals("Yo-yo", repository!!.getProduct(1).name)
    }

    @Test
    @Throws(EntityNotFound::class)
    fun orderContainsProduct() {
        Assertions.assertEquals(3, repository!!.getOrder(1).products.size)
    }
}