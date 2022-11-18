package http;

import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
@Lazy
class TestConfig {
    @LocalServerPort
    private int port;

    @Bean
    public Customers getCustomerService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:" + port)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        return retrofit.create(Customers.class);
    }
}