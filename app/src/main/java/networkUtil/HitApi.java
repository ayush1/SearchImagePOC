package networkUtil;

import android.util.Log;

import listener.ApiListener;
import model.PhotosResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.AppConstants;
import utils.ConstUrl;

public class HitApi {

    static ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    static ApiListener apiListner;

    public static void getImages(String requestString, int pageNumber){
        Call<PhotosResponse> call = apiInterface.requestImages(ConstUrl.FLICKR_METHOD, AppConstants.API_KEY,
                pageNumber, requestString, ConstUrl.FLICKR_FORMAT, ConstUrl.FLICKR_JSON_CALLBACK);
        Log.d("RequestURL:", call.request().url().toString());

        call.enqueue(new Callback<PhotosResponse>() {
            @Override
            public void onResponse(Call<PhotosResponse> call, Response<PhotosResponse> response) {
                apiListner.onSuccess(response);
            }

            @Override
            public void onFailure(Call<PhotosResponse> call, Throwable t) {
                apiListner.onFailure();
            }
        });
    }

    public static void bindListner(ApiListener listner){
        apiListner = listner;
    }

}
