package com.example.camconvertorapp;
import android.content.Context;
import java.io.Serializable;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import org.jetbrains.annotations.NotNull;


@Entity
class Frequency implements Serializable {

    @PrimaryKey
    @NonNull public String type;// holds for the frequency type, e.g: Currency, Temperature, Length, etc..


    @ColumnInfo(name = "sourceType")// holds for the frequency source type the user want to convert, e.g: Currency : Dollar
    @NonNull public String source;


    @ColumnInfo(name = "targetType")// holds for the frequency target type the user want converting to, e.g: Currency : Euro
    @NonNull public String target;


    public void setType(@NotNull String type){
        this.type = type;
    }
    public void setSource(@NotNull String source){
        this.source = source;
    }
    public void setTarget(@NotNull String target){
        this.target = target;
    }


    @NotNull
    public String getType(){
        return this.type;
    }
    public String getSourceType(){
        return this.source;
    }
    public String getTargetType(){
        return this.target;
    }
}

@Dao
interface FreqDao extends Serializable {

    @Query("SELECT * FROM Frequency")
    LiveData<List<Frequency>> getAll();


    @Query("SELECT * FROM Frequency WHERE type LIKE :type_name")
    Frequency findByType(String type_name);


    @Query("DELETE FROM Frequency ")
    void deleteAll();

    @Query("DELETE FROM Frequency WHERE type LIKE :type_name")
    void deleteByType(String type_name);

    @Insert
    void insertAll(Frequency... frequencies);

    @Delete
    void delete(Frequency frequency);
}


// instantiating the room db
@Database(entities = {Frequency.class}, version = 1, exportSchema = false)
abstract class AppDatabase extends RoomDatabase implements Serializable{
    public abstract FreqDao freqDao();
    private static volatile AppDatabase INSTANCE;
    static AppDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (AppDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,"camConvertor - database").build();
                }
            }
        }
        return INSTANCE;
    }


}


