package us.vslt.poketeam.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import us.vslt.poketeam.data.DAO.regionDAO
import us.vslt.poketeam.data.Model.PokemonDataRegion
import us.vslt.poketeam.data.Model.region

@Database(entities = [region::class,PokemonDataRegion::class],version = 2,exportSchema = false)
abstract class RoomDB: RoomDatabase() {

    abstract fun regionDao(): regionDAO

    companion object{
        @Volatile
        private  var INSTANCE : RoomDB? = null

        fun getInstance(context: Context):RoomDB{
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room
                    .databaseBuilder(context, RoomDB::class.java, "poketeams_Database")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE=instance
                return instance
            }
        }
    }
}