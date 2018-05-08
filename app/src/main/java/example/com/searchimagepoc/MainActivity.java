package example.com.searchimagepoc;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import adapter.GridAdapter;
import asynctask.FetchOfflineData;
import db.AppDatabase;
import listener.OfflineDataListener;
import model.Photo;
import model.PhotosResponse;
import listener.ApiListener;
import networkUtil.HitApi;
import retrofit2.Response;
import utils.AppConstants;
import utils.CommonUtil;
import utils.DatabaseInitialiser;

public class MainActivity extends AppCompatActivity implements ApiListener,
                AbsListView.OnScrollListener, OfflineDataListener{

    private Context context;
    private ArrayList<String> photoUrls = new ArrayList();
    private ArrayList<String> tempPhotoUrls= new ArrayList();

    private GridAdapter gridAdapter;
    private GridView gridView;

    private ProgressDialog progressDialog;
    private String searchString;

    private AppDatabase appDatabase;
    private OfflineDataListener dataListener;

    private int pageCounter = AppConstants.DEFAULT_PAGE_NUMBER;
    private int visibleThreshold = 5;
    private int currentPage = 0;
    private int previousTotal = 0;
    private boolean isLoading = true;
    private boolean lastPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.grid);
        context = this;
        dataListener = this;

        gridView.setOnScrollListener(this);
        gridView.setVerticalScrollBarEnabled(false);

        progressDialog = new ProgressDialog(this);
        appDatabase = AppDatabase.getAppDatabaseInstance(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        HitApi.bindListner(this);
    }

    /**
     * Searchbar
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.home_menu, menu);
        MenuItem search = menu.findItem(R.id.search);

        SearchView searchView = (SearchView) search.getActionView();
        searchView.setFocusable(false);
        searchView.setQueryHint("Search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String str) {
                searchString = str;
                initVariables();
                if(CommonUtil.isNetworkAvailable(context)){
                    HitApi.getImages(searchString, pageCounter);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                }else{
                    FetchOfflineData fetchOfflineData = new FetchOfflineData(dataListener, appDatabase);
                    fetchOfflineData.execute(searchString);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    private void initVariables() {
        tempPhotoUrls.clear();
        previousTotal = 0;
        currentPage = 0;
        pageCounter = AppConstants.DEFAULT_PAGE_NUMBER;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.item_1:
                gridView.setNumColumns(2);
                break;
            case R.id.item_2:
                gridView.setNumColumns(3);
                break;
            case R.id.item_3:
                gridView.setNumColumns(4);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Flickr API response
     * @param response
     */
    @Override
    public void onSuccess(Response<PhotosResponse> response) {
        ArrayList photosList = response.body().getPhotos().photo;
        photoUrls.clear();

        for(int i = 0; i < photosList.size(); i++){
            Photo photo = (Photo) photosList.get(i);
            photoUrls.add(CommonUtil.generatePhotoURL(photo.getFarm(), photo.getId(),
                    photo.getSecret(), photo.getServer()));
        }

        if(progressDialog.isShowing()){
            progressDialog.dismiss();
            this.getCurrentFocus().clearFocus();
        }

        for(int i = 0; i < photosList.size(); i++){
            tempPhotoUrls.add(photoUrls.get(i));
            DatabaseInitialiser.populateAsync(appDatabase, photoUrls.get(i), searchString);
        }

        if(tempPhotoUrls.size() == 100){
            gridAdapter = new GridAdapter(tempPhotoUrls, this);
            gridView.setAdapter(gridAdapter);
        }else{
            gridAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onFailure() {
        Toast.makeText(this, R.string.no_data_fetch_from_API, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    /**
     * Pagination logic
     * @param view
     * @param firstVisibleItem
     * @param visibleItemCount
     * @param totalItemCount
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (isLoading) {
            if (totalItemCount > previousTotal) {
                isLoading = false;
                previousTotal = totalItemCount;
                currentPage++;

                if (currentPage + 1 > 80) {
                    lastPage = true;
                }
            }
        }
        if (!lastPage && !isLoading &&
                (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            pageCounter++;
            HitApi.getImages(searchString, pageCounter);
            isLoading = true;
        }
    }

    /**
     * Offline data listener
     * @param photosUrls
     */
    @Override
    public void onGetOfflineData(List<String> photosUrls) {
        if(photosUrls.size() > 0){
            gridAdapter = new GridAdapter((ArrayList) photosUrls, this);
            gridView.setAdapter(gridAdapter);
        }else{
            Toast.makeText(this, "Internet is not available and there is no " +
                    "offline data related to the search", Toast.LENGTH_SHORT).show();
        }
    }
}
