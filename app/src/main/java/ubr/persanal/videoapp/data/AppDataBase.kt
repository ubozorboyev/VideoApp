package ubr.persanal.videoapp.data

import android.content.Context
import androidx.room.*
import ubr.persanal.videoapp.data.dao.MovieDao
import ubr.persanal.videoapp.util.UUIDConverter

@Database(entities = [MovieData::class], version = 2, exportSchema = false)
@TypeConverters(UUIDConverter::class)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getMovieDao(): MovieDao

    companion object{

        @Volatile
        var instance :AppDataBase? = null

        fun getInstance(context: Context){
                instance = Room.databaseBuilder(context, AppDataBase::class.java,"movie")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}