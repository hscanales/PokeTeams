package us.vslt.poketeam.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import us.vslt.poketeam.data.DAO.pokemonDAO
import us.vslt.poketeam.data.DAO.regionDAO
import us.vslt.poketeam.data.DAO.regionExtendedDAO
import us.vslt.poketeam.data.Model.*

@Database(entities = [region::class,PokemonDataRegion::class,Pokemon::class,Pokemon_Regional::class],version = 7,exportSchema = false)
@TypeConverters(SpriteConverter::class,pokeTypeConverter::class,pokeTypeConverter::class)
abstract class RoomDB: RoomDatabase() {

    abstract fun regionDao(): regionDAO
    abstract fun pokemonDAO(): pokemonDAO
    abstract fun regionExtendedDAO() : regionExtendedDAO
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