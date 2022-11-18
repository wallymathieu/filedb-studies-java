package http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
@Lazy
class TestConfig {
    @Autowired
    private Retrofit retrofit;

    @Bean
    public Customers getCustomers() {
        return retrofit.create(Customers.class);
    }
    @Bean
    public Products getProducts() {
        return retrofit.create(Products.class);
    }
}
@Configuration
@Lazy
class RetrofitConfig{
    @LocalServerPort
    private int port;

    @Bean
    public Retrofit getRetrofit() {
        return new Retrofit.Builder()
            .baseUrl("http://localhost:" + port)
            .addConverterFactory(JacksonConverterFactory.create())
            .build();
    }
}