package http;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.gewalli.Application;
import se.gewalli.entities.Customer;

import java.util.List;

import static http.RequestHelper.assertListOfV1Body;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestHelper{

    public static void assertListOfV1Body(ResponseEntity<List<Customer>> exchange) {
        List<Customer> body = exchange.getBody();
        assertEquals(2, body.size());
        assertEquals("Firstname",((Customer) body.get(0)).firstname);
    }
    public static ResponseEntity<List<Customer>> queryCustomers(TestRestTemplate restTemplate, int port){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        return restTemplate.exchange("http://localhost:" + port + "/api/customers", HttpMethod.GET, entity, new ParameterizedTypeReference<>() {
        });
    }
    public static ResponseEntity<Customer> postCustomer(TestRestTemplate restTemplate, int port, Customer customer){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity<Customer> entity = new HttpEntity<>(customer, headers);
        return restTemplate.exchange("http://localhost:" + port + "/api/customers", HttpMethod.POST, entity, new ParameterizedTypeReference<>() {
        });
    }
}
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes=Application.class)
public class HttpRequestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private ResponseEntity<List<Customer>> queryParamApi(){
        return RequestHelper.queryCustomers(restTemplate,port);
    }
    private ResponseEntity<Customer> postParamApi(Customer customer){
        return RequestHelper.postCustomer(restTemplate,port,customer);
    }

    @Test
    public void testSuccessfullyQueryVersionParameterV1() throws Exception {
        postParamApi(new Customer(1,"Firstname","Lastname",1));
        postParamApi(new Customer(2,"Firstname","Lastname",1));
        ResponseEntity<List<Customer>> exchange = queryParamApi();
        assertEquals(HttpStatus.OK, exchange.getStatusCode());
        assertListOfV1Body(exchange);
    }

}