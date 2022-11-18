package http;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import se.gewalli.controllers.CustomersController.CreateCustomer;
import se.gewalli.entities.Customer;

interface Customers {
    @GET("/api/customers")
    Call<List<Customer>> listCustomers();

    @POST("/api/customers")
    Call<Customer> postCustomer(@Body CreateCustomer customer);
}