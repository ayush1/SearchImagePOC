package asynctask;

import android.os.AsyncTask;
import java.util.List;
import db.AppDatabase;
import listener.OfflineDataListener;

public class FetchOfflineData extends AsyncTask<String, Void, List<String>> {
    private AppDatabase database;
    OfflineDataListener dataListener;

    public FetchOfflineData(OfflineDataListener dataListener, AppDatabase database) {
        this.database = database;
        this.dataListener = dataListener;
    }

    @Override
    protected List<String> doInBackground(String... strings) {
        return database.persistedDao().getPhotoUrls(strings[0]);
    }

    @Override
    protected void onPostExecute(List<String> photosUrls) {
        super.onPostExecute(photosUrls);
        dataListener.onGetOfflineData(photosUrls);
    }

}
