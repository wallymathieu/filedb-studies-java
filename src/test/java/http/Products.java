package http;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import se.gewalli.controllers.ProductsController.CreateProduct;
import se.gewalli.entities.Product;

public interface Products {
    @GET("/api/products")
    Call<List<Product>> list();

    @POST("/api/products")
    Call<Product> post(@Body CreateProduct customer);
}