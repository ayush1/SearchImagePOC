package db;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {PersistedData.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{

    private static AppDatabase dbInstance;
    public abstract PersistedDao persistedDao();

    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    public static AppDatabase getAppDatabaseInstance(Context context){
        if(dbInstance == null){
            dbInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "offline-database").build();
        }
        return dbInstance;
    }

    public static void destroyInstance(){
        dbInstance = null;
    }

}
