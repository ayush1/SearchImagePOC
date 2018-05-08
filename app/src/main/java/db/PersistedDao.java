package db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface PersistedDao {

    @Query("SELECT key_url FROM persisted WHERE search_key LIKE :searchKey")
    List<String> getPhotoUrls(String searchKey);

    @Insert
    void insertAll(PersistedData... persistedData);

}
