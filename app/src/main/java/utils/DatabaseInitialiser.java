package utils;

import android.os.AsyncTask;

import db.AppDatabase;
import db.PersistedData;

public class DatabaseInitialiser {

    public static void populateAsync(AppDatabase database, String photo, String key){
        PopulateAsync async = new PopulateAsync(database, photo, key);
        async.execute();
    }

    private static PersistedData addPersisted(final AppDatabase db, PersistedData persisted) {
        db.persistedDao().insertAll(persisted);
        return persisted;
    }

    public static void populateDatabase(AppDatabase database, String photoUrl, String searchKey){
        PersistedData persisted = new PersistedData();
        persisted.setKey(searchKey);
        persisted.setKeyUrl(photoUrl);

        addPersisted(database, persisted);

    }

    private static class PopulateAsync extends AsyncTask<Void, Void, Void> {
        private AppDatabase database;
        private String photoUrl;
        private String searchKey;

        public PopulateAsync(AppDatabase database, String photo, String key) {
            this.database = database;
            this.photoUrl = photo;
            this.searchKey = key;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            populateDatabase(database, photoUrl, searchKey);
            return null;
        }
    }
}
