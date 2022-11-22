package http;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import retrofit2.Response;
import se.gewalli.controllers.CustomersController.CreateCustomer;
import se.gewalli.controllers.OrdersController.AddProduct;
import se.gewalli.controllers.OrdersController.CreateOrder;
import se.gewalli.controllers.ProductsController.CreateProduct;
import se.gewalli.entities.Customer;
import se.gewalli.entities.Order;
import se.gewalli.entities.Product;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = ApplicationUnderTest.class)
public class HttpRequestTest {
    @Autowired
    private Customers customers;
    @Autowired
    private Products products;
    @Autowired
    private Orders orders;

    @Test
    public void testCanCreateCustomersAndListThem() throws Exception {
        customers.post(new CreateCustomer(1, "Firstname", "Lastname")).execute();
        customers.post(new CreateCustomer(2, "Firstname", "Lastname")).execute();
        Response<List<Customer>> exchange = customers.list().execute();
        assertEquals(HttpStatus.OK.value(), exchange.code());
        assertEquals("Firstname", ((Customer) exchange.body().get(0)).firstName());
    }

    @Test
    public void testCanCreateAndGetCustomer() throws Exception {
        customers.post(new CreateCustomer(1, "Firstname", "Lastname")).execute();
        Response<Customer> exchange = customers.get(1).execute();
        assertEquals(HttpStatus.OK.value(), exchange.code());
        assertEquals("Firstname", exchange.body().firstName());
    }

    @Test
    public void testCanCreateProductsAndListThem() throws Exception {
        products.post(new CreateProduct(1, 10, "product1")).execute();
        products.post(new CreateProduct(2, 20, "product2")).execute();
        Response<List<Product>> exchange = products.list().execute();
        assertEquals(HttpStatus.OK.value(), exchange.code());
        assertEquals(2, exchange.body().size());
    }

    @Test
    public void testCanCreateAndGetProduct() throws Exception {
        products.post(new CreateProduct(1, 10, "product1")).execute();
        Response<Product> exchange = products.get(1).execute();
        assertEquals(HttpStatus.OK.value(), exchange.code());
        assertEquals("product1", exchange.body().name());
    }

    @Test
    public void testCanCreateAndGetOrder() throws Exception {
        int customer = 3;
        int productId = 1;
        int orderId = 2;
        customers.post(new CreateCustomer(customer, "Firstname", "Lastname")).execute();
        orders.post(new CreateOrder(orderId, customer)).execute();
        products.post(new CreateProduct(productId, 10, "product1")).execute();
        Response<Order> productAddedResponse = orders.addProduct(orderId, new AddProduct(productId)).execute();
        assertEquals(HttpStatus.OK.value(), productAddedResponse.code());
        Response<Order> exchange = orders.get(orderId).execute();
        assertEquals(HttpStatus.OK.value(), exchange.code());
        Order body = exchange.body();
        assertNotNull(body);
        assertEquals("Firstname", body.customer().firstName());
        assertEquals(1, body.products().size());
    }
}