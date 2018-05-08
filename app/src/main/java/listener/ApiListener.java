package listener;

import model.PhotosResponse;
import retrofit2.Response;

public interface ApiListener {
    void onSuccess(Response<PhotosResponse> response);
    void onFailure();
}
