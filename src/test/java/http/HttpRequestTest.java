package http;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import se.gewalli.entities.Customer;

import java.nio.file.LinkOption;
import java.util.List;

import static http.RequestHelper.assertListOfV1Body;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestHelper{

    public static void assertListOfV1Body(ResponseEntity<List<Customer>> exchange) {
        List<Customer> body = exchange.getBody();
        assertEquals(2, body.size());
        //assertThat( body.get(0)).isInstanceOf(Customer.class);
        assertEquals("Firstname",((Customer) body.get(0)).firstname);
    }
    public static ResponseEntity<List<Customer>> queryAcceptApi(TestRestTemplate restTemplate, int port, String accept){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", accept);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        return restTemplate.exchange("http://localhost:" + port + "/users", HttpMethod.GET, entity, new ParameterizedTypeReference<List<Customer>>() {});
    }
    public static ResponseEntity<List<Customer>> queryParamApi(TestRestTemplate restTemplate, int port, String version){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        return restTemplate.exchange("http://localhost:" + port + "/user"+(version==null?"":"?api-version="+version), HttpMethod.GET, entity, new ParameterizedTypeReference<List<Customer>>() {});
    }

}
//@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    private ResponseEntity<List<Customer>> queryAcceptApi(String accept){
        return RequestHelper.queryAcceptApi(restTemplate,port,accept);
    }
    private ResponseEntity<List<Customer>> queryParamApi(String version){
        return RequestHelper.queryParamApi(restTemplate,port,version);
    }

    @Test
    public void testSuccessfullyQueryVersionParameterV1() throws Exception {
        ResponseEntity<List<Customer>> exchange = queryParamApi("1.0");
        assertEquals(HttpStatus.OK, exchange.getStatusCode());
        assertEquals("application/json;charset=UTF-8", exchange.getHeaders().get("Content-Type").get(0));
        assertListOfV1Body(exchange);
    }

    @Test
    public void testSuccessfullyQueryVersionParameterDefaultV2() throws Exception {
        ResponseEntity<List<Customer>> exchange = queryParamApi(null);
        assertEquals(HttpStatus.OK, exchange.getStatusCode());
        assertEquals("application/json;charset=UTF-8", exchange.getHeaders().get("Content-Type").get(0));
        assertListOfV1Body(exchange);
    }

    @Test
    public void testSuccessfullyQueryAcceptV1() throws Exception {
        ResponseEntity<List<Customer>> exchange =queryAcceptApi("application/se.gewalli.users.v1+json");

        assertEquals(HttpStatus.OK, exchange.getStatusCode());
        assertEquals("application/se.gewalli.users.v1+json;charset=UTF-8", exchange.getHeaders().get("Content-Type").get(0));
        assertListOfV1Body(exchange);
    }

}