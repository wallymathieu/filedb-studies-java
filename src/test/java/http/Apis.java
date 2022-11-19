package http;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import se.gewalli.controllers.CustomersController.CreateCustomer;
import se.gewalli.controllers.OrdersController.AddProduct;
import se.gewalli.controllers.OrdersController.CreateOrder;
import se.gewalli.controllers.ProductsController.CreateProduct;
import se.gewalli.entities.Customer;
import se.gewalli.entities.Order;
import se.gewalli.entities.Product;

interface Products {
    @GET("/api/products")
    Call<List<Product>> list();
    @GET("/api/products/{id}")
    Call<Product> get(@Path("id") int id);
    @POST("/api/products")
    Call<Product> post(@Body CreateProduct customer);
}

interface Customers {
    @GET("/api/customers")
    Call<List<Customer>> list();
    @GET("/api/customers/{id}")
    Call<Customer> get(@Path("id") int id);
    @POST("/api/customers")
    Call<Customer> post(@Body CreateCustomer customer);
}

interface Orders {
    @GET("/api/orders")
    Call<List<Order>> list();
    @GET("/api/orders/{id}")
    Call<Order> get(@Path("id") int id);
    @POST("/api/orders")
    Call<Order> post(@Body CreateOrder createOrder);
    @POST("/api/orders/{id}/products")
    Call<Order> addProduct(@Path("id") int id, @Body AddProduct createOrder);
}