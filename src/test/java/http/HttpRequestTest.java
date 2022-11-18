package http;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import retrofit2.Response;
import se.gewalli.controllers.CustomersController.CreateCustomer;
import se.gewalli.controllers.ProductsController.CreateProduct;
import se.gewalli.entities.Customer;
import se.gewalli.entities.Product;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = ApplicationUnderTest.class)
public class HttpRequestTest {
    @Autowired
    private Customers customers;
    @Autowired
    private Products products;

    public void assertListOfV1Body(List<Customer> body) {
        assertEquals(2, body.size());
        assertEquals("Firstname", ((Customer) body.get(0)).firstname);
    }

    @Test
    public void testCanCreateCustomersAndListThem() throws Exception {
        customers.postCustomer(new CreateCustomer(1, "Firstname", "Lastname")).execute();
        customers.postCustomer(new CreateCustomer(2, "Firstname", "Lastname")).execute();
        Response<List<Customer>> exchange = customers.listCustomers().execute();
        assertEquals(HttpStatus.OK.value(), exchange.code());
        assertListOfV1Body(exchange.body());
    }
    @Test
    public void testCanCreateProductsAndListThem() throws Exception {
        products.post(new CreateProduct(1, 10, "product1")).execute();
        products.post(new CreateProduct(2, 20, "product2")).execute();
        Response<List<Product>> exchange = products.list().execute();
        assertEquals(HttpStatus.OK.value(), exchange.code());
        assertEquals(2, exchange.body().size());
    }
}