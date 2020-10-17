package us.vslt.poketeam.data.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import us.vslt.poketeam.data.Model.Pokemon
import us.vslt.poketeam.data.Model.region

@Dao
interface pokemonDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pokemon: Pokemon)

    @Query("SELECT * FROM pokemon")
    fun getAll(): LiveData<List<Pokemon>>

    @Query("DELETE FROM pokemon")
    suspend fun nuke()
}