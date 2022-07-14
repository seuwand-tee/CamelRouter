package api;

import util.CollectionFormats.*;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.MultipartBody;

import domain.ErrorMessage;
import domain.Sale;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SalesApi {
  /**
   * Add new sale
   * Add a new sale to the system.
   * @param sale  (required)
   * @return Call&lt;Void&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("sales")
  Call<Void> addSale(
    @retrofit2.http.Body Sale sale
  );

  /**
   * Delete a sale
   * Delete the sale matching the given ID.
   * @param id  (required)
   * @return Call&lt;Void&gt;
   */
  @DELETE("sales/sale/{id}")
  Call<Void> deleteSale(
    @retrofit2.http.Path("id") String id
  );

  /**
   * Get all sales
   * Get all of the sales.
   * @return Call&lt;List&lt;Sale&gt;&gt;
   */
  @GET("sales")
  Call<List<Sale>> getAllSales();
    

}
