package db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "persisted")
public class PersistedData {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "search_key")
    private String key;

    @ColumnInfo(name = "key_url")
    private String keyUrl;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKeyUrl() {
        return keyUrl;
    }

    public void setKeyUrl(String keyUrl) {
        this.keyUrl = keyUrl;
    }
}
