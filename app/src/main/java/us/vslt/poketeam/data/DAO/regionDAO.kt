package us.vslt.poketeam.data.DAO


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import us.vslt.poketeam.data.Model.PokemonDataRegion
import us.vslt.poketeam.data.Model.region


@Dao
interface regionDAO{


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(region:region)

    @Query("SELECT * FROM region")
    fun getAll(): LiveData<List<region>>

    @Query("DELETE FROM region")
    suspend fun nuke()


    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    suspend fun insertPokemonRegion(data : PokemonDataRegion)

    @Query ( "DELETE FROM poke_region")
    suspend fun nukePokemons()

}