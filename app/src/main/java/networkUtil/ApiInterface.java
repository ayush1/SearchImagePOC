package networkUtil;

import model.Photos;
import model.PhotosResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import utils.ConstUrl;

public interface ApiInterface {

    @GET("rest/")
    Call<PhotosResponse> requestImages(@Query("method") String method,
                                       @Query("api_key") String key,
                                       @Query("page") int page,
                                       @Query("tags") String searchString,
                                       @Query("format") String format,
                                       @Query("nojsoncallback") String jsonCallBack);

}
