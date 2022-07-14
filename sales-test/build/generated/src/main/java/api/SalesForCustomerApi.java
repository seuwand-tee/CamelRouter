package api;

import util.CollectionFormats.*;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.MultipartBody;

import domain.ErrorMessage;
import domain.Sale;
import domain.Summary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SalesForCustomerApi {
  /**
   * Get sales for customer
   * Get all sales for the customer matching the given ID.
   * @param id  (required)
   * @return Call&lt;List&lt;Sale&gt;&gt;
   */
  @GET("sales/customer/{id}")
  Call<List<Sale>> getSalesForCustomer(
    @retrofit2.http.Path("id") String id
  );

  /**
   * Get sales summary for customer
   * Get a summary of sales for the customer matching the given ID.
   * @param id  (required)
   * @return Call&lt;Summary&gt;
   */
  @GET("sales/customer/{id}/summary")
  Call<Summary> getSummary(
    @retrofit2.http.Path("id") String id
  );

}
