package us.vslt.poketeam.data.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import us.vslt.poketeam.data.Model.Pokemon_Regional

@Dao
interface regionExtendedDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonRegion(data: Pokemon_Regional)

    @Query("DELETE FROM poke_regional")
    suspend fun nukePokemons()

    @Query("SELECT * FROM poke_regional")
    fun getpokemonfromregion(): LiveData<List<Pokemon_Regional>>

}